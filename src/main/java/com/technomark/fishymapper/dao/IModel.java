package com.technomark.fishymapper.dao;

/**
 * Created by troy on 6/28/17.
 */
public interface IModel {
    public Long getId();
    public void setId(Long id);
    public IModel clone();
}
