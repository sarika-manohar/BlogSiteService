package com.blogsite.blog.service.controller;

import com.blogsite.blog.service.entity.Blog;
import com.blogsite.blog.service.service.BlogDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blogsite")
@Slf4j
public class BlogController {

    @Autowired
    private BlogDataService blogDataService;

    @PostMapping(value="/user/blogs/add/{blogname}")
    public String addBlog(@RequestBody Blog blog) throws Exception {
        log.info("inside add blog controller");
        blogDataService.ValidateblogDetails(blog);
        return blog.getBlogname() + " added successfully";
    }

    @DeleteMapping (value="/user/delete/{blogname}")
    public String deleteBlog(@PathVariable String blogname) throws Exception {
        log.info("inside delete blog controller");
        blogDataService.deleteBlog(blogname);
        return blogname+" deleted successfully";
    }

    @GetMapping(value="/user/getall")
    public List<Blog> getAllBlogs(@RequestParam(required =false) String category) throws Exception {
        if(category==null) {
            return blogDataService.getAllBlogs();
        }
        else{
            return blogDataService.getAllBlogsByCategory(category);
        }
    }

    @GetMapping(value="/user/getBlogs")
    public List<Blog> getMyBlogs() throws Exception {
        return blogDataService.getMyBlogs();
    }

    @GetMapping(value="/user/getBlogsBtwDateRange")
    public List<Blog> getBlogsBetweenRange(@Param("startDate") Date date1, @Param("endDate") Date date2) throws Exception {
        return blogDataService.getBlogsBetweenDateRange(date1,date2);
    }
}
