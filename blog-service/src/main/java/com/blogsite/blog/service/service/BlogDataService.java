package com.blogsite.blog.service.service;

import com.blogsite.blog.service.entity.Blog;
import com.blogsite.blog.service.exception.ServiceException;
import com.blogsite.blog.service.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class BlogDataService {

    @Autowired
    private BlogRepository blogRepository;

    public Blog ValidateblogDetails(Blog blog) throws Exception {

            log.info("inside blog service method");
            String[] noOfWords = blog.getArticle().split("\\s+");
            if (blog.getBlogname() != null && blog.getBlogname().length() < 20) {
                throw new ServiceException("Blog name must contain more than 20 characters");
            }
            if (blog.getCategory() != null && blog.getCategory().length() < 5) {
                throw new ServiceException("Category must contain more than 5 characters");
            }
            if (blog.getArticle() != null && noOfWords.length < 10) {
                throw new ServiceException("Article must contain more than 1000 words");
            }
            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            blog.setTimestamp(now);
            if (now.toString() == null) {
                throw new ServiceException("timestamp is not provided by the service");
            }
            return blogRepository.save(blog);
    }

    public void deleteBlog(String blogname) throws Exception {
        log.info("inside delete blog method");
        Blog deleteBlog = blogRepository.findByBlogname(blogname);
        if(deleteBlog==null){
            throw new ServiceException(blogname+" doesn't exist");
        }
        blogRepository.delete(deleteBlog);
    }

}
