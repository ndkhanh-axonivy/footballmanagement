package com.axonactive.footballmanagement.service;

import com.axonactive.footballmanagement.dao.PlayerDao;
import com.axonactive.footballmanagement.dao.TeamDao;
import com.axonactive.footballmanagement.entities.PlayerEntity;
import com.axonactive.footballmanagement.entities.TeamEntity;
import com.axonactive.footballmanagement.entities.TeamPlayedEntity;
import com.axonactive.footballmanagement.rest.exception.CustomException;
import com.axonactive.footballmanagement.rest.exception.errormessages.ErrorConstant;
import com.axonactive.footballmanagement.rest.request.PlayerRequest;
import com.axonactive.footballmanagement.service.dto.PlayerDto;
import com.axonactive.footballmanagement.service.mapper.PlayerMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PlayerService {

    @Inject
    private PlayerDao playerDao;

    @Inject
    private TeamDao teamDao;

    @Inject
    private PlayerMapper playerMapper;

    public PlayerDto getPlayerDtoById(Long id) {
        TeamPlayedEntity currentTeamPlayed = getCurrentTeamPlayedByPlayerId(id);
        if (currentTeamPlayed != null) {
            return playerMapper.toDto(currentTeamPlayed);
        }

        PlayerEntity player = getPlayerById(id);
        if (player == null) {
            throw new CustomException(ErrorConstant.MSG_PLAYER_NOT_FOUND, Response.Status.NOT_FOUND);
        }
        return playerMapper.toDto(player);
    }

    public List<PlayerDto> getAllPlayers() {
        List<PlayerEntity> players = playerDao.getAllPlayers();
        return players.stream()
                .map(player -> {
                    TeamPlayedEntity currentTeamPlayed = getCurrentTeamPlayedByPlayerId(player.getId());
                    if (currentTeamPlayed != null) {
                        return playerMapper.toDto(currentTeamPlayed);
                    }
                    return playerMapper.toDto(player);
                })
                .collect(Collectors.toList());
    }

    public TeamPlayedEntity getCurrentTeamPlayedByPlayerId(Long id) {
        try {
            return playerDao.getCurrentTeamPlayedByPlayerId(id);
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    public PlayerEntity getPlayerById(Long id) {
        try {
            return playerDao.getPlayerById(id);
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    public List<PlayerDto> getCurrentActivePlayersByTeamId(Long id) {
        TeamEntity team = teamDao.getTeamById(id);
        if (team == null) {
            throw new CustomException(ErrorConstant.MSG_TEAM_NOT_FOUND, Response.Status.NOT_FOUND);
        }
        return playerMapper.toDtos(team.getAllPlayers().stream()
                .filter(TeamPlayedEntity::getIsActive)
                .collect(Collectors.toList()));
    }

    public PlayerDto createPlayer(PlayerEntity player) {
        return playerMapper.toDto(playerDao.savePlayer(player));
    }

    public PlayerDto updatePlayer(PlayerEntity player) {
        return playerMapper.toDto(playerDao.savePlayer(player));
    }

    public void validateGeneralAddRequest(List<?> objects) {
        isRequestEmpty(objects);
    }

    private void isRequestEmpty(List<?> objects) {
        if (objects.isEmpty()) {
            throw new CustomException(ErrorConstant.MSG_REQUEST_EMPTY, Response.Status.BAD_REQUEST);
        }
    }

    public void validateAddPlayerRequest(List<PlayerRequest> playerRequests) {
        // Check active of 1 club do not larger than maxNumberEachClub of League

        // More...
    }
}
