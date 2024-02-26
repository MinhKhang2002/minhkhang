package com.laptrinhjavaweb.dao.impl;

import com.laptrinhjavaweb.dao.ICommentDAO;
import com.laptrinhjavaweb.mapper.CommentMapper;
import com.laptrinhjavaweb.mapper.NewMapper;
import com.laptrinhjavaweb.mapper.UserMapper;
import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.Pageble;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.util.List;

public class CommentDAO extends AbstractDAO<CommentModel> implements ICommentDAO {

    @Override
    public Long addComment(CommentModel commentModel) {
        StringBuilder sql = new StringBuilder("INSERT INTO comment (content, user_id, new_id, createddate)");
        sql.append(" VALUES (?, ?, ?, ?)");
        Long commentId = insert(sql.toString(), commentModel.getContent(), commentModel.getUserId(), commentModel.getNewId(),
                commentModel.getCreateDate());
        return commentId;
    }

    @Override
    public CommentModel findOne(Long id) {
        String sql = "select * from comment WHERE id = ?";
        List<CommentModel> cmts = query(sql, new CommentMapper(), id);
        return cmts.isEmpty() ? null : cmts.get(0);
    }

    @Override
    public List<CommentModel> getCommentsByNewId(Long newId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM comment AS u");
        sql.append(" INNER JOIN user AS r ON r.id = u.user_id INNER JOIN news AS n ON n.id = u.new_id");
        sql.append(" WHERE u.new_id = ?");
        List<CommentModel> comment = query(sql.toString(), new CommentMapper(), newId);
        return comment;
    }

    @Override
    public void deleteCommentAndNews(Long newId) {
        String sql = "DELETE FROM comment WHERE new_id = ?";
        update(sql, newId);
    }

    @Override
    public void deleteComment(long id) {
        String sql = "DELETE FROM comment WHERE id = ?";
        update(sql, id);
    }

    @Override
    public List<CommentModel> findAll(Pageble pageble) {
        StringBuilder sql = new StringBuilder("SELECT * FROM comment AS u INNER JOIN user AS r ON r.id = u.user_id INNER JOIN news AS n ON n.id = u.new_id");
        if(pageble.getSorter() != null && StringUtils.isNotBlank(pageble.getSorter().getSortName()) && StringUtils.isNotBlank(pageble.getSorter().getSortBy())) {
            sql.append(" order by "+pageble.getSorter().getSortName()+" "+pageble.getSorter().getSortBy()+"");
        }
        if(pageble.getOffset() != null && pageble.getLimit() != null) {
            sql.append(" LIMIT "+pageble.getOffset()+", "+pageble.getLimit()+"");
        }
        return query(sql.toString(), new CommentMapper());
    }
    /*public List<CommentModel> findAll() {
        StringBuilder sql = new StringBuilder("SELECT * FROM comment");
        *//*if(pageble.getSorter() != null && StringUtils.isNotBlank(pageble.getSorter().getSortName()) && StringUtils.isNotBlank(pageble.getSorter().getSortBy())) {
            sql.append(" order by "+pageble.getSorter().getSortName()+" "+pageble.getSorter().getSortBy()+"");
        }
        if(pageble.getOffset() != null && pageble.getLimit() != null) {
            sql.append(" LIMIT "+pageble.getOffset()+", "+pageble.getLimit()+"");
        }*//*
        return query(sql.toString(), new CommentMapper());
    }*/

    @Override
    public int getTotalItem() {
        String sql = "select count(*) from comment";
        return count(sql);
    }


}
