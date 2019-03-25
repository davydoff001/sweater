package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;
    
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {       
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter, 
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        
        Page<Message> page;
        
        if (filter != null && !filter.isEmpty()) {
            page = messageRepo.findByTag(filter, pageable);
        } else {
            page = messageRepo.findAll(pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        
        Page<Message> page;
        
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(file, message);
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
        
        page = messageRepo.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "main";
    }  

    private void saveFile(MultipartFile file, Message message) throws IOException, IllegalStateException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidfile = UUID.randomUUID().toString();
            String resultFileName = uuidfile + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        }
    }
    
    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble
    ){

        Page<Message> page;
        page = messageRepo.findByAuthor(user, pageble);
        
        Set<Message> messages = user.getMessages();
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("url", "/user-messages/" + user.getId());
        model.addAttribute("page", page);
       
        return "userMessages";
    }
    
    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file) throws IOException{
        
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            
            saveFile(file, message);
            messageRepo.save(message);
        }
        
        return "redirect:/user-messages/"+user;
    }
}
