package com.blog.BloggingProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.BloggingProject.model.post;

public interface repo extends JpaRepository<post, Integer> {
}
