package com.localcinemacrud.service;

import com.localcinemacrud.model.Title;

public interface Operations {
    void save(Title title);

   void getAll();

    void getById(String id);

    void update(String id);

    void delete(String id);




}
