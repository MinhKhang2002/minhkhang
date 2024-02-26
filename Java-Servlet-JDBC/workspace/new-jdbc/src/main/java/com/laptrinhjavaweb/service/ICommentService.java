package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.paging.Pageble;

import java.util.List;

public interface ICommentService {
    /*CommentModel findComents (CommentModel commentModel);*/
    CommentModel addComment (CommentModel commentModel);
    CommentModel findOne(Long id);
    List<CommentModel> getCommentsByNewId (Long newId);
    void deleteCommentAndNews(Long newId);
    List<CommentModel> findAll(Pageble pageble);
    void deleteComment(long[] ids);
//    List<CommentModel> findAll();
    int getTotalItem();
//    CommentModel getCommentsByNewId(Long newId);
}
