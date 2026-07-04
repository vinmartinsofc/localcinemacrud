package com.localcinemacrud.service;
import com.localcinemacrud.model.Title;

import java.util.Map;

public interface Operations {
    void save(Title title);

    void delete(String id);

    void update(String id);

    Map<String, Title> getAll();

    void getById(String id);


}
