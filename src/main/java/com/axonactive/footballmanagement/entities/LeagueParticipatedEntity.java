package com.axonactive.footballmanagement.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "league_participated",
        uniqueConstraints= @UniqueConstraint(columnNames={"team_id", "league_id"}))
public class LeagueParticipatedEntity implements IGenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "league_id")
    private LeagueEntity league;

    private Integer rank;

    private RecordEntity record;

}
