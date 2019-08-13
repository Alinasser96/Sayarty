package com.alyndroid.syarty.di.component;

import com.alyndroid.syarty.pojo.Comments;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component
public interface CommentsComponent {
    Comments getComments();
}
