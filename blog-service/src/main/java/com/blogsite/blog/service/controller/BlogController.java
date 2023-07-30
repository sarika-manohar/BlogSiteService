package com.blogsite.blog.service.controller;

import com.blogsite.blog.service.config.KafkaProducerConfig;
import com.blogsite.blog.service.entity.Blog;
import com.blogsite.blog.service.service.BlogDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/blogsite")
@Slf4j
public class BlogController {

    @Autowired
    private BlogDataService blogDataService;
    KafkaProducerConfig config = new KafkaProducerConfig();

    @PostMapping(value="/user/blogs/add")
    public String addBlog(@RequestBody Blog blog) throws Exception {
        log.info("inside add blog controller");
        blogDataService.ValidateblogDetails(blog);
        config.sendLogToKafka(blog.getBlogname() + " added successfully");
        return blog.getBlogname() + " added successfully";
    }

    @DeleteMapping (value="/user/delete/{blogname}")
    public String deleteBlog(@PathVariable String blogname) throws Exception {
        log.info("inside delete blog controller");
        blogDataService.deleteBlog(blogname);
        config.sendLogToKafka(blogname+" deleted successfully");
        return blogname+" deleted successfully";
    }

    @GetMapping(value="/user/getall")
    public List<Blog> getAllBlogs(@RequestParam(required =false) String category) throws Exception {
        if(category==null) {
            config.sendLogToKafka("Blogs retrieved");
            return blogDataService.getAllBlogs();
        }
        else{
            config.sendLogToKafka("Blogs retrieved by category: "+category);
            return blogDataService.getAllBlogsByCategory(category);
        }
    }

    @GetMapping(value="/user/getBlogs")
    public List<Blog> getMyBlogs() throws Exception {
        return blogDataService.getMyBlogs();
    }

    @GetMapping(value="/user/getBlogsBtwDateRange")
    public List<Blog> getBlogsBetweenRange(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws Exception {
        return blogDataService.getBlogsBetweenDateRange(startDate,endDate);
    }
}
