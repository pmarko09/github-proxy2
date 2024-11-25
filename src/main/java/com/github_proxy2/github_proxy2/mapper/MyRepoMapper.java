package com.github_proxy2.github_proxy2.mapper;

import com.github_proxy2.github_proxy2.model.dto.MyRepoDto;
import com.github_proxy2.github_proxy2.model.entity.MyRepo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MyRepoMapper {

    MyRepoDto toDto(MyRepo myRepo);
}
