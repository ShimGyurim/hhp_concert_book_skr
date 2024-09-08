package com.helloworld.quickstart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ITEM")
public class itemEntity {

    @Id
    private String id;

    
    private String name;
}
