package com.project.aclub.service;
import com.project.aclub.dto.ParticipationRequest;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Participation;
import com.project.aclub.entity.User;
import com.project.aclub.repository.ClubRepository;
import com.project.aclub.repository.ParticipationRepository;
import com.project.aclub.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public ParticipationService(ParticipationRepository participationRepository,
                                UserRepository userRepository,
                                ClubRepository clubRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public boolean createParticipation(ParticipationRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        Optional<Club> clubOpt = clubRepository.findById(request.getClubId());

        if (userOpt.isEmpty() || clubOpt.isEmpty()) {
            return false;
        }

        Participation participation = new Participation();
        participation.setUser(userOpt.get());
        participation.setClub(clubOpt.get());
        participation.setRole(request.getRole());

        participationRepository.save(participation);
        return true;
    }
}
