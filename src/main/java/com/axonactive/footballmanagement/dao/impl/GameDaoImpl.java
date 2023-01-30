package com.axonactive.footballmanagement.dao.impl;

import com.axonactive.footballmanagement.dao.GameDao;
import com.axonactive.footballmanagement.entities.GameEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class GameDaoImpl implements GameDao {

    @PersistenceContext(unitName = "football")
    EntityManager em;

    @Override
    public List<GameEntity> getMatchesByDate(LocalDateTime fromLocalDateTime, LocalDateTime toLocalDateTime) {
        return em.createQuery("SELECT g FROM GameEntity g WHERE g.dateTimeOfGame>=:fromDateTime" +
                        " AND g.dateTimeOfGame<=:toDateTime", GameEntity.class)
                .setParameter("fromDateTime", fromLocalDateTime)
                .setParameter("toDateTime", toLocalDateTime)
                .getResultList();
    }
}
