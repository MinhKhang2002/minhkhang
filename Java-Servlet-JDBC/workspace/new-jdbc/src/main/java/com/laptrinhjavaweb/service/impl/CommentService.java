package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.dao.ICommentDAO;
import com.laptrinhjavaweb.dao.INewDAO;
import com.laptrinhjavaweb.dao.IUserDAO;
import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.Pageble;
import com.laptrinhjavaweb.service.ICommentService;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

public class CommentService implements ICommentService {
    @Inject
    private ICommentDAO commentDAO;
    @Inject
    private IUserDAO userDAO;
    @Inject
    private INewDAO newDAO;

    @Override
    public CommentModel addComment(CommentModel commentModel) {
        commentModel.setCreateDate(new Timestamp(System.currentTimeMillis()));
        UserModel userModel = userDAO.findOne(commentModel.getUserId());
        NewModel newModel = newDAO.findOne(commentModel.getNewId());
        commentModel.setUserId(userModel.getId());
        commentModel.setNewId(newModel.getId());
        Long commentId = commentDAO.addComment(commentModel);
        return commentDAO.findOne(commentId);
    }

    @Override
    public CommentModel findOne(Long id) {
        CommentModel commentModel = commentDAO.findOne(id);
        return commentModel;
    }

    @Override
    public List<CommentModel> getCommentsByNewId(Long newId) {
        return commentDAO.getCommentsByNewId(newId);
    }

    @Override
    public void deleteCommentAndNews(Long newId) {
        commentDAO.deleteCommentAndNews(newId);
    }

    @Override
    public List<CommentModel> findAll(Pageble pageble) {
        return commentDAO.findAll(pageble);
    }

    @Override
    public void deleteComment(long[] ids) {
        for(long id : ids){
            commentDAO.deleteComment(id);
        }
    }

    @Override
    public int getTotalItem() {
        return commentDAO.getTotalItem();
    }
}
