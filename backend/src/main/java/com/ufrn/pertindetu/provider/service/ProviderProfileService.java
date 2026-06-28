package com.ufrn.pertindetu.provider.service;

import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.provider.dto.ProviderProfileDTO;
import com.ufrn.pertindetu.provider.model.ProviderProfile;
import com.ufrn.pertindetu.provider.repository.ProviderProfileRepository;
import com.ufrn.pertindetu.user.model.User;
import com.ufrn.pertindetu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderProfileService {

    private final ProviderProfileRepository providerRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProviderProfileDTO createOrUpdate(String userEmail, ProviderProfileDTO dto) {
        User user = userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        if (!user.getRole().name().equals("PROVIDER")) {
            throw new BusinessException("Apenas usuários com perfil de Prestador podem criar uma vitrine profissional.", HttpStatus.FORBIDDEN);
        }

        ProviderProfile profile = providerRepository.findByUserId(user.getId())
                .orElse(new ProviderProfile());

        profile.setUser(user);
        profile.setCategory(dto.getCategory());
        profile.setBio(dto.getBio());
        profile.setWhatsappPhone(dto.getWhatsappPhone());
        profile.setNeighborhood(dto.getNeighborhood());
        profile.setCity(dto.getCity());
        profile.setState(dto.getState().toUpperCase());

        ProviderProfile saved = providerRepository.save(profile);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public ProviderProfileDTO getMyProfile(String userEmail) {
        User user = userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        ProviderProfile profile = providerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException("Você ainda não criou sua vitrine profissional.", HttpStatus.NOT_FOUND));

        return toDTO(profile);
    }

    private ProviderProfileDTO toDTO(ProviderProfile entity) {
        ProviderProfileDTO dto = new ProviderProfileDTO();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setBio(entity.getBio());
        dto.setWhatsappPhone(entity.getWhatsappPhone());
        dto.setNeighborhood(entity.getNeighborhood());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        return dto;
    }
}