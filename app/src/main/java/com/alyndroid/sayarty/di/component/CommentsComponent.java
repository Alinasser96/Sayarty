package com.alyndroid.sayarty.di.component;

import com.alyndroid.sayarty.pojo.Comments;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component
public interface CommentsComponent {
    Comments getComments();
}
