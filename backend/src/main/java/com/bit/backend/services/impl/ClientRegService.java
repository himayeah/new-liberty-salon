package com.bit.backend.services.impl;

import com.bit.backend.dtos.ClientLifeTimeValueDto;
import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.dtos.ClientRegTotalVisitsDto;
import com.bit.backend.entities.ClientRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.ClientRegMapper;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.services.ClientRegServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.bit.backend.config.EmailSender;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
public class ClientRegService implements ClientRegServiceI {

    private final ClientRegRepository clientRegRepository;
    private final ClientRegMapper clientRegMapper;
    private final PasswordEncoder passwordEncoder; // Injected for password hashing
    private final EmailSender emailSender; // Injected to dispatch reset email

    public ClientRegService(ClientRegRepository clientRegRepository, ClientRegMapper clientRegMapper, PasswordEncoder passwordEncoder, EmailSender emailSender) {
        this.clientRegRepository = clientRegRepository;
        this.clientRegMapper = clientRegMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }

    @Override
    public ClientRegDto addClientReg(ClientRegDto clientRegDto) {
        try {
            ClientRegEntity clientRegEntity = clientRegMapper.toClientRegEntity(clientRegDto);
            System.out.println("Client First Name 3  :" + clientRegEntity.getFirstName());
            ClientRegEntity savedItem = clientRegRepository.save(clientRegEntity);
            ClientRegDto savedDto = clientRegMapper.toClientRegDto(savedItem);

            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<ClientRegDto> getData() {
        try {
            List<ClientRegEntity> clientRegEntityList = clientRegRepository.findAll();
            List<ClientRegDto> clientRegDtoList = clientRegMapper.toClientRegDtoList(clientRegEntityList);
            System.out.println("Getting all Client Data: " + clientRegDtoList);
            return clientRegDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientRegDto updateClientReg(long id, ClientRegDto clientRegDto) {
        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }
            ClientRegEntity existing = optionalClientRegEntity.get();
            ClientRegEntity newClientRegEntity = clientRegMapper.toClientRegEntity(clientRegDto);
            newClientRegEntity.setId(id);

            // Preserve existing fields that are not present in the update form
            if (newClientRegEntity.getPhoto() == null || newClientRegEntity.getPhoto().isEmpty()) {
                newClientRegEntity.setPhoto(existing.getPhoto());
            }
            if (newClientRegEntity.getTotalVisits() <= 0) {
                newClientRegEntity.setTotalVisits(existing.getTotalVisits());
            }
            if (newClientRegEntity.getLastVisitedDate() == null || newClientRegEntity.getLastVisitedDate().isEmpty()) {
                newClientRegEntity.setLastVisitedDate(existing.getLastVisitedDate());
            }
            if (newClientRegEntity.getRegistrationDate() == null
                    || newClientRegEntity.getRegistrationDate().isEmpty()) {
                newClientRegEntity.setRegistrationDate(existing.getRegistrationDate());
            }

            List<ClientLifeTimeValueDto> clientLifeTimeValueDtoList = clientRegRepository.getClientLifetimeValue();
            for (ClientLifeTimeValueDto clientLifeTimeValueDto : clientLifeTimeValueDtoList) {
                if (clientLifeTimeValueDto.getClientId() == id) {
                    newClientRegEntity.setLifetimeValue(clientLifeTimeValueDto.getLifetimeValue());
                    break;
                }
            }

            ClientRegEntity clientRegEntity = clientRegRepository.save(newClientRegEntity);
            ClientRegDto clientRegDtoRes = clientRegMapper.toClientRegDto(clientRegEntity);
            System.out.println("update Successfully: " + clientRegDtoRes.getFirstName());
            return clientRegDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientRegDto getById(long id) {
        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }
            return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClientRegDto deleteClientReg(long id) {

        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findById(id);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }

            clientRegRepository.deleteById(id);
            return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(
                    "Cannot delete this client because they have active appointments, billings, or other associated records.",
                    HttpStatus.CONFLICT);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dashboard card (New Clients within last 30 days)
    @Override
    public long countClientRegistrationsLast30Days() {
        return clientRegRepository.countClientRegistrationsLast30Days();
    }

    // Search Client for Public Booking Login
    @Override
    public ClientRegDto findByFirstNameAndEmail(String firstName, String email) {
        try {
            Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository.findByFirstNameAndEmail(firstName,
                    email);
            if (!optionalClientRegEntity.isPresent()) {
                throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
            }
            return clientRegMapper.toClientRegDto(optionalClientRegEntity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get Client Total Visits
    // run query, get id and count results, add them to clientreg entity columns so
    // they get saved in db
    // convert entity back to dto and send response back to controller
    @Override
    public List<ClientRegTotalVisitsDto> calculateClientVisits() {
        try {
            List<ClientRegTotalVisitsDto> clientRegTotalVisitsDtoList = clientRegRepository.getClientTotalVisits();
            System.out.println("Client Total Visits DTO List: " + clientRegTotalVisitsDtoList);
            for (ClientRegTotalVisitsDto clientRegTotalVisitsDto : clientRegTotalVisitsDtoList) {
                if (clientRegTotalVisitsDto.getClientId() != null) {
                    Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository
                            .findById(clientRegTotalVisitsDto.getClientId());
                    if (optionalClientRegEntity.isPresent()) {
                        ClientRegEntity clientRegEntity = optionalClientRegEntity.get();
                        clientRegEntity.setTotalVisits(clientRegTotalVisitsDto.getTotalVisits());
                        clientRegRepository.save(clientRegEntity);
                    }
                }
            }
            return clientRegTotalVisitsDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ClientLifeTimeValueDto> calculateClientLifeTimeValue() {
        try {
            List<ClientLifeTimeValueDto> clientLifeTimeValueDtoList = clientRegRepository.getClientLifetimeValue();
            // clientLifeTimeValueDto can be names as anything but ClientLifeTimeValueDto
            // must match the type stored inside the list
            for (ClientLifeTimeValueDto clientLifeTimeValueDto : clientLifeTimeValueDtoList) {
                Optional<ClientRegEntity> optionalClientRegEntity = clientRegRepository
                        .findById(clientLifeTimeValueDto.getClientId());
                if (!optionalClientRegEntity.isPresent()) {
                    throw new AppException("Client Reg Does Not Exist", HttpStatus.NOT_FOUND);
                }
                ClientRegEntity clientRegEntity = optionalClientRegEntity.get();
                clientRegEntity.setLifetimeValue(clientLifeTimeValueDto.getLifetimeValue());
                clientRegRepository.save(clientRegEntity);
            }
            return clientLifeTimeValueDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Validates password strength according to the custom rules:
    // More than 8 characters long, contain characters (letters), a number, and a special character
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 9) {
            throw new AppException("Password must be more than 8 characters long", HttpStatus.BAD_REQUEST);
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        if (!hasLetter || !hasDigit || !hasSpecial) {
            throw new AppException("Password must contain letters, numbers, and at least one special character", HttpStatus.BAD_REQUEST);
        }
    }

    // Checks client email status: REGISTER (new client), SET_PASSWORD (existing but no password), or LOGIN (password exists)
    @Override
    public String checkEmailStatus(String email) {
        try {
            Optional<ClientRegEntity> optionalClient = clientRegRepository.findByEmail(email);
            if (!optionalClient.isPresent()) {
                return "REGISTER";
            }
            ClientRegEntity client = optionalClient.get();
            if (client.getPassword() == null || client.getPassword().isBlank()) {
                return "SET_PASSWORD";
            }
            return "LOGIN";
        } catch (Exception e) {
            throw new AppException("Failed to check email status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Registers a brand new client with hashed password
    @Override
    public ClientRegDto registerClient(ClientRegDto clientRegDto) {
        try {
            Optional<ClientRegEntity> existingClient = clientRegRepository.findByEmail(clientRegDto.getEmail());
            if (existingClient.isPresent()) {
                throw new AppException("Email is already registered", HttpStatus.BAD_REQUEST);
            }

            validatePasswordStrength(clientRegDto.getPassword());

            ClientRegEntity clientRegEntity = clientRegMapper.toClientRegEntity(clientRegDto);
            clientRegEntity.setPassword(passwordEncoder.encode(clientRegDto.getPassword()));

            ClientRegEntity savedItem = clientRegRepository.save(clientRegEntity);
            return clientRegMapper.toClientRegDto(savedItem);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Sets password for an existing client who has no password stored yet
    @Override
    public ClientRegDto setClientPassword(String email, String password) {
        try {
            ClientRegEntity client = clientRegRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException("Client not found", HttpStatus.NOT_FOUND));

            validatePasswordStrength(password);

            client.setPassword(passwordEncoder.encode(password));
            ClientRegEntity savedItem = clientRegRepository.save(client);
            return clientRegMapper.toClientRegDto(savedItem);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to set password: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Authenticates client login via email + password credentials
    @Override
    public ClientRegDto loginClient(String email, String password) {
        try {
            ClientRegEntity client = clientRegRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED));

            if (client.getPassword() == null || !passwordEncoder.matches(password, client.getPassword())) {
                throw new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }

            return clientRegMapper.toClientRegDto(client);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Login failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dispatches a password reset link to the client's email address
    @Override
    public void forgotPassword(String email) {
        try {
            ClientRegEntity client = clientRegRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException("Client not found with this email", HttpStatus.NOT_FOUND));

            String token = UUID.randomUUID().toString();
            client.setResetToken(token);
            client.setResetTokenExpiry(LocalDateTime.now().plusHours(2)); // Token valid for 2 hours
            clientRegRepository.save(client);

            // Construct link pointing to the frontend router link
            String resetLink = "http://localhost:4200/public-booking?token=" + token;
            String body = String.format(
                    "Hello %s,%n%n" +
                    "We received a request to reset your password. Please click the link below to create a new password:%n" +
                    "%s%n%n" +
                    "If you did not request this, you can ignore this email. The link will expire in 2 hours.%n%n" +
                    "Best Regards,%n" +
                    "New Liberty Salon Team",
                    client.getFirstName(), resetLink);

            emailSender.sendSimpleEmail(email, "Password Reset Link - New Liberty Salon", body);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to process forgot password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Validates a reset token and sets the client's password
    @Override
    public void resetPassword(String token, String password) {
        try {
            ClientRegEntity client = clientRegRepository.findByResetToken(token)
                    .orElseThrow(() -> new AppException("Invalid or expired password reset token", HttpStatus.BAD_REQUEST));

            if (client.getResetTokenExpiry() == null || client.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new AppException("Password reset token has expired", HttpStatus.BAD_REQUEST);
            }

            validatePasswordStrength(password);

            // Update password & clear token fields
            client.setPassword(passwordEncoder.encode(password));
            client.setResetToken(null);
            client.setResetTokenExpiry(null);
            clientRegRepository.save(client);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to reset password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
