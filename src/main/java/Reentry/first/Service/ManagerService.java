package Reentry.first.Service;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.DTO.EmployeeDTO.RespondEmployeeDTO;
import Reentry.first.DTO.ManagerDTO.ManagerMapper;
import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
import Reentry.first.DTO.ManagerDTO.RespondManagerDTO;
import Reentry.first.Entity.Employee;
import Reentry.first.Entity.Manager;
import Reentry.first.Entity.WorkGroup;
import Reentry.first.Repository.EmployeeRepository;
import Reentry.first.Repository.ManagerRepository;
import Reentry.first.Repository.WorkGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final WorkGroupRepository workGroupRepository;
    private final ManagerMapper managerMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public ManagerService(ManagerRepository managerRepository,
                          ManagerMapper managerMapper,
                          WorkGroupRepository workGroupRepository,
                          EmployeeRepository employeeRepository,
                          EmployeeMapper employeeMapper) {

        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.workGroupRepository = workGroupRepository;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }


    /// CREATE
    public RespondManagerDTO createManager(RequestManagerDTO dto) {

        Manager manager = managerMapper.toEntity(dto);

        managerRepository.save(manager);

        return managerMapper.toRespondDTO(manager);
    }


    /// GET BY ID
    public RespondManagerDTO getManagerById(Long id) {

        Manager manager = managerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Manager not found"));

        return managerMapper.toRespondDTO(manager);
    }


    /// GET ALL
    public List<RespondManagerDTO> getAllManagers() {

        List<Manager> managers = managerRepository.findAll();

        return managerMapper.respondManagerDTOList(managers);
    }





    /// UPDATE
    public RespondManagerDTO updateManager(Long id,
                                           RequestManagerDTO dto) {

        Manager manager = managerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Manager not found"));

        manager.setName(dto.getName());

        managerRepository.save(manager);

        return managerMapper.toRespondDTO(manager);
    }


    /// DELETE
    public void deleteManager(Long id) {

        Manager manager = managerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Manager not found"));

        managerRepository.delete(manager);
    }








}