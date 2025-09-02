package ib.api.bank.domain.repository;

import ib.api.bank.domain.model.Portion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortionRepository extends JpaRepository<Portion, Long> {
}
