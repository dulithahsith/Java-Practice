package com.libApp.LibraryMgtSystem.repositories;

import com.libApp.LibraryMgtSystem.models.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, String> {

}