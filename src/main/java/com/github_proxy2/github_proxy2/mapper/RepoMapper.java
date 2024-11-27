package com.github_proxy2.github_proxy2.mapper;

import com.github_proxy2.github_proxy2.model.dto.RepoDto;
import com.github_proxy2.github_proxy2.model.entity.Repo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepoMapper {

    RepoDto toDto(Repo repo);
}
