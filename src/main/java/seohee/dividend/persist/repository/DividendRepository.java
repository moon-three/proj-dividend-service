package seohee.dividend.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seohee.dividend.persist.entity.DividendEntity;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
}
