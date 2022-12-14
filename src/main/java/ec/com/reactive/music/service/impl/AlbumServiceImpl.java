package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.domain.entities.Album;
import ec.com.reactive.music.repository.IAlbumRepository;
import ec.com.reactive.music.service.IAlbumService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlbumServiceImpl implements IAlbumService {
    @Autowired
    private IAlbumRepository iAlbumRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Mono<ResponseEntity<Flux<AlbumDTO>>> findAllAlbums() {

        return Mono.justOrEmpty(new ResponseEntity<>(this.iAlbumRepository
                                .findAll()
                                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                                .map(this::entityToDTO),HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @Override
    public Mono<ResponseEntity<AlbumDTO>> findAlbumById(String id) {
        //Handling errors
        return this.iAlbumRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString()))) //Capture the error
                .map(this::entityToDTO)
                .map(albumDTO -> new ResponseEntity<>(albumDTO, HttpStatus.FOUND)) //Mono<ResponseEntity<AlbumDTO>>
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST))); //Handle the error
    }

    @Override
    public Mono<ResponseEntity<AlbumDTO>> saveAlbum(AlbumDTO albumDTO) {
        return this.iAlbumRepository
                .save(DTOToEntity(albumDTO))
                .map(this::entityToDTO)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED)); //Mono<ResponseEntity<AlbumDTO>>
    }





    @Override
    public Album DTOToEntity(AlbumDTO albumDTO) {
        return this.modelMapper.map(albumDTO, Album.class);
    }

    @Override
    public AlbumDTO entityToDTO(Album album) {
        return this.modelMapper.map(album,AlbumDTO.class);
    }
}
