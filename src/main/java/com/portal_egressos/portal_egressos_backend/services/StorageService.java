package com.portal_egressos.portal_egressos_backend.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;


@Service
public class StorageService {
    public String salvarImagem(MultipartFile imagem){
        if (imagem == null) {
            return null;
        }

        String contentType = imagem.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new RegraNegocioRunTime("Formato de arquivo inválido. Envie uma imagem.");
        }

        String diretorioUpload = "uploads";
        File dir = new File(diretorioUpload);
        if (!dir.exists()) dir.mkdirs();

        String extensao = imagem.getOriginalFilename().substring(imagem.getOriginalFilename().lastIndexOf("."));
        String nomeArquivo = UUID.randomUUID() + extensao;

        Path caminho = Paths.get(diretorioUpload, nomeArquivo);
        try {

            Files.write(caminho, imagem.getBytes());
        } catch (IOException e) {
            throw new RegraNegocioRunTime("Erro ao salvar imagem.");
        }
        

        return nomeArquivo;
    }
}
