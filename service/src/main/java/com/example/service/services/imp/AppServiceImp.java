package com.example.service.services.imp;

import com.example.service.exception.MyBadRequestException;
import com.example.service.models.dtos.TareaDto;
import com.example.service.models.entities.Tareas;
import com.example.service.models.entities.UserEntity;
import com.example.service.repositories.TareasRepository;
import com.example.service.repositories.UserRepository;
import com.example.service.services.AppService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppServiceImp implements AppService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TareasRepository tareasRepository;
    @Autowired
    private ModelMapper modelMapper;

    private UserEntity getAuthentication(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username).orElseThrow(()-> new MyBadRequestException("Vuelve a iniciar seccion"));
    }

    private Tareas findByIdAndUsername(Long id, UserEntity user){
        return tareasRepository.findByIdAndUsername(user.getUsername(), id)
                .orElseThrow(()-> new MyBadRequestException("Tarea no encontrada"));
    }

    @Override
    @Transactional
    public List<Tareas> findAll() {
        UserEntity user = getAuthentication();
        return tareasRepository.findByUsername(user.getUsername());
    }

    @Override
    @Transactional
    public Tareas updateTareaById(Long id, TareaDto tareaDto) {
        Tareas tarea = findByIdAndUsername(id, getAuthentication());
        modelMapper.getConfiguration().setPropertyCondition(ctx -> ctx.getSource() != null);
        modelMapper.map(tareaDto, tarea);
        return tareasRepository.save(tarea);
    }

    @Override
    @Transactional
    public Tareas save(TareaDto tareaDto) {
        Tareas tarea = modelMapper.map(tareaDto, Tareas.class);
        tarea.setUser(getAuthentication());
        return tareasRepository.save(tarea);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var res = findByIdAndUsername(id, getAuthentication());
        tareasRepository.deleteById(res.getId());
    }


}
