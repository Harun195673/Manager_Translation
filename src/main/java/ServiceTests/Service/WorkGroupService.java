package ServiceTests.Service;


import ServiceTests.DTO.WorkgroupDTO.RequestWorkGroupDTO;
import ServiceTests.DTO.WorkgroupDTO.RespondWorkGroupDTO;
import ServiceTests.DTO.WorkgroupDTO.WorkGroupMapper;
import ServiceTests.Entity.Manager;
import ServiceTests.Entity.WorkGroup;
import ServiceTests.Exceptions.DuplicateResourceException;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Exceptions.ResourceNotFoundException;
import ServiceTests.Repository.ManagerRepository;
import ServiceTests.Repository.WorkGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkGroupService {

    private final WorkGroupRepository workGroupRepository;
    private final ManagerRepository managerRepository;
    private final WorkGroupMapper workGroupMapper;

    public WorkGroupService(WorkGroupRepository workGroupRepository,
                            ManagerRepository managerRepository,
                            WorkGroupMapper workGroupMapper) {

        this.workGroupRepository = workGroupRepository;
        this.managerRepository = managerRepository;
        this.workGroupMapper = workGroupMapper;
    }


    /// CREATE
    public RespondWorkGroupDTO createWorkGroup(RequestWorkGroupDTO dto) {

        if (workGroupRepository.existsByName(dto.getName())){
            throw new DuplicateResourceException("Workgroup already exists");
        }

        Manager manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));

        WorkGroup workGroup = workGroupMapper.toEntity(dto);
        workGroup.setManager(manager);
        workGroupRepository.save(workGroup);

        return workGroupMapper.toRespondDTO(workGroup);
    }


    /// GET BY ID
    public RespondWorkGroupDTO getWorkGroupById(Long id) {

        WorkGroup workGroup = workGroupRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkGroup not found"));

        return workGroupMapper.toRespondDTO(workGroup);
    }


    /// GET ALL
    public List<RespondWorkGroupDTO> getAllWorkGroups() {

        List<WorkGroup> workGroups = workGroupRepository.findAll();

        return workGroups.stream()
                .map(workGroupMapper::toRespondDTO)
                .toList();
    }


    /// UPDATE
    public RespondWorkGroupDTO updateWorkGroup(Long id, RequestWorkGroupDTO dto) {

        WorkGroup workGroup = workGroupRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkGroup not found"));

        if (workGroupRepository.existsByName(dto.getName())
                && !workGroup.getName().equals(dto.getName())) {
            throw new DuplicateResourceException("Workgroup already exists");
        }

        workGroup.setName(dto.getName());

        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Manager not found"));

            workGroup.setManager(manager);
        }

        workGroupRepository.save(workGroup);

        return workGroupMapper.toRespondDTO(workGroup);
    }


    /// DELETE
    public void deleteWorkGroup(Long id) {

        WorkGroup workGroup = workGroupRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkGroup not found"));

        if (workGroup.getEmployeeList().size() > 0){
            throw new InvalidOperationException("Cannot delete workGroup with employees.");
        }

        workGroupRepository.delete(workGroup);
    }
}