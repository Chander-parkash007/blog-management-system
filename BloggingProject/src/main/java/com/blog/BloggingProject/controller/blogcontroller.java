package com.blog.BloggingProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.BloggingProject.model.post;
import com.blog.BloggingProject.repository.repo;

@Controller
public class blogcontroller {

    @Autowired
    private repo Repo;

    // Homepage - list all posts
    @GetMapping("/")
    public String viewhomepage(Model model) {
        List<post> posts = Repo.findAll();
        model.addAttribute("listPosts", posts);
        return "index";
    }

    // Show create new post form
    @GetMapping("/new")
    public String newpost(Model model) {
        model.addAttribute("post", new post());
        return "new_post";
    }

    // Save new post
    @PostMapping("/posts/create")
    public String savepost(@ModelAttribute("post") post Post) {
        Repo.save(Post);
        return "redirect:/";
    }

    // FIXED: Handle the manage-post page with POST requests
    @PostMapping("/manage-post")
    public String managePost(@RequestParam("postId") int postId,
            @RequestParam("action") String action,
            Model model) {
        if ("edit".equals(action)) {
            return "redirect:/edit/" + postId;
        } else if ("delete".equals(action)) {
            return "redirect:/delete/" + postId;
        }
        return "redirect:/";
    }

    // Show edit form - CHANGED URL from /manage-post/{id} to /edit/{id}
    @GetMapping("/edit/{id}")
    public String editpost(@PathVariable("id") int id, Model model) {
        Optional<post> optionalPost = Repo.findById(id);
        if (optionalPost.isPresent()) {
            model.addAttribute("post", optionalPost.get());
            return "editpost"; // must match editpost.html
        } else {
            return "redirect:/";
        }
    }

    // Handle update submission
    @PostMapping("/posts/update")
    public String updatePost(@ModelAttribute("post") post Post) {
        Optional<post> existingPost = Repo.findById(Post.getId());
        if (existingPost.isPresent()) {
            post updatedPost = existingPost.get();
            updatedPost.setTitle(Post.getTitle());
            updatedPost.setAuthor(Post.getAuthor());
            updatedPost.setContent(Post.getContent());
            Repo.save(updatedPost);
        }
        return "redirect:/";
    }

    // Show delete confirmation page
    @GetMapping("/delete/{id}")
    public String deleteConfirmation(@PathVariable("id") int id, Model model) {
        Optional<post> optionalPost = Repo.findById(id);
        if (optionalPost.isPresent()) {
            model.addAttribute("post", optionalPost.get());
            return "deletepost";
        } else {
            return "redirect:/";
        }
    }

    // Handle delete submission
    @PostMapping("/posts/delete")
    public String deletePost(@RequestParam("id") int id) {
        Repo.deleteById(id);
        return "redirect:/";
    }
}
