package com.Project.AppApplication.dto;

import com.Project.AppApplication.domain.candidato.Candidato;

public class CandidatoDTO {
    private boolean formularioPreenchido;
    private Candidato candidato;

    public void Response(boolean formularioPreenchido, Candidato candidato) {
        this.formularioPreenchido = formularioPreenchido;
        this.candidato = candidato;
    }

    public boolean isFormularioPreenchido() {
        return formularioPreenchido;
    }

    public Candidato getCandidato() {
        return candidato;
    }
}
