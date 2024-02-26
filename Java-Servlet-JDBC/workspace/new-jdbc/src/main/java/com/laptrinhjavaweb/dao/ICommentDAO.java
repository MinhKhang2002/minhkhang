package com.laptrinhjavaweb.dao;

import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.paging.Pageble;

import java.util.List;

public interface ICommentDAO extends GenericDAO<CommentModel> {
    Long addComment (CommentModel commentModel);
    CommentModel findOne(Long id);
    List<CommentModel> getCommentsByNewId (Long newId);
    void deleteCommentAndNews(Long newId);
    void deleteComment(long id);
    List<CommentModel> findAll(Pageble pageble);
//    List<CommentModel> findAll();
    int getTotalItem();
//    CommentModel getCommentsByNewId(Long newId);
}
