package cz.prague.vida.vocab.repository;

import cz.prague.vida.vocab.model.WordGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordGroupRepository extends JpaRepository<WordGroup, Long> {

    @Query(value = "select w from WordGroup w where w.lessonId = :lessonId")
    List<WordGroup> findWordGroups(long lessonId);
}
