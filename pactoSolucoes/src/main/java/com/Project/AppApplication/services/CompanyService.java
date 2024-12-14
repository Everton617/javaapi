package com.Project.AppApplication.services;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.empresa.Company;
import com.Project.AppApplication.domain.empresa.Job;
import com.Project.AppApplication.repository.company.CompanyRepository;
import com.Project.AppApplication.repository.company.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Company> listarCompany() {
        return companyRepository.findAll();
    }

    public Job createJob(Long companyId, Job job) {
        Optional<Company> companyOptional = companyRepository.findById(companyId);

        if (companyOptional.isEmpty()) {
            throw new IllegalArgumentException("Empresa com ID " + companyId + " não encontrada.");
        }
        Company company = companyOptional.get();
        job.setCompany(company);
        return jobRepository.save(job);
    }

    public Company createCompany(Company company) {
        if (company.getEmail() == null || company.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email da empresa é obrigatório.");
        }

        if (company.getNome() == null || company.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome da empresa é obrigatório.");
        }

        if (company.getPassword() == null || company.getPassword().isEmpty()) {
            throw new IllegalArgumentException("A senha da empresa é obrigatória.");
        }


        return companyRepository.save(company);
    }
}
