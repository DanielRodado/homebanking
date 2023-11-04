package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    @Override
    public Set<LoanDTO> getAllLoansDTO() {
        return getAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsLoanById(Long id) {
        return loanRepository.existsById(id);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
