package com.s23010535.govisaviya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import java.util.List;
import java.text.DecimalFormat;

public class FinancialHelpActivity extends Activity {
    
    // UI Components
    private ImageButton btnBack, btnCalculator;
    private Button btnLoanCalculator, btnGrants, btnCalculate, btnContactAdvisor;
    private EditText etLoanAmount, etInterestRate, etLoanTerm;
    private LinearLayout resultsContainer;
    private TextView tvMonthlyPayment, tvTotalPayment;
    private RecyclerView rvPrograms;
    
    // Data
    private List<FinancialProgram> programs;
    private ProgramAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_help);
        
        initializeViews();
        initializeData();
        setupClickListeners();
        setupInputValidation();
        setupRecyclerView();
        addSamplePrograms();
    }
    
    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnCalculator = findViewById(R.id.btnCalculator);
        btnLoanCalculator = findViewById(R.id.btnLoanCalculator);
        btnGrants = findViewById(R.id.btnGrants);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnContactAdvisor = findViewById(R.id.btnContactAdvisor);
        etLoanAmount = findViewById(R.id.etLoanAmount);
        etInterestRate = findViewById(R.id.etInterestRate);
        etLoanTerm = findViewById(R.id.etLoanTerm);
        resultsContainer = findViewById(R.id.resultsContainer);
        tvMonthlyPayment = findViewById(R.id.tvMonthlyPayment);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        rvPrograms = findViewById(R.id.rvPrograms);
    }
    
    private void initializeData() {
        programs = new ArrayList<>();
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnCalculator.setOnClickListener(v -> showCalculator());
        btnLoanCalculator.setOnClickListener(v -> showCalculator());
        btnGrants.setOnClickListener(v -> showGrantsInfo());
        btnCalculate.setOnClickListener(v -> calculateLoan());
        btnContactAdvisor.setOnClickListener(v -> contactAdvisor());
    }
    
    private void setupInputValidation() {
        // Add input validation for loan calculator
        etLoanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                validateInputs();
            }
        });
        
        etInterestRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                validateInputs();
            }
        });
        
        etLoanTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                validateInputs();
            }
        });
    }
    
    private void setupRecyclerView() {
        adapter = new ProgramAdapter(programs);
        rvPrograms.setLayoutManager(new LinearLayoutManager(this));
        rvPrograms.setAdapter(adapter);
    }
    
    private void addSamplePrograms() {
        programs.add(new FinancialProgram(
            "Agricultural Loan Scheme",
            "Low-interest loans for farming equipment and inputs",
            "Government",
            "Up to Rs. 500,000",
            "5.5% per annum",
            "5-10 years"
        ));
        
        programs.add(new FinancialProgram(
            "Crop Insurance Program",
            "Insurance coverage for crop damage and losses",
            "Insurance",
            "Full crop value",
            "2% of insured amount",
            "Annual renewal"
        ));
        
        programs.add(new FinancialProgram(
            "Farmer Grant Program",
            "Non-repayable grants for sustainable farming",
            "Government",
            "Up to Rs. 100,000",
            "0% (Grant)",
            "One-time"
        ));
        
        programs.add(new FinancialProgram(
            "Equipment Financing",
            "Financing for agricultural machinery and tools",
            "Bank",
            "Up to Rs. 1,000,000",
            "8.5% per annum",
            "3-7 years"
        ));
        
        programs.add(new FinancialProgram(
            "Microfinance Support",
            "Small loans for small-scale farmers",
            "Microfinance",
            "Up to Rs. 50,000",
            "12% per annum",
            "1-3 years"
        ));
        
        adapter.notifyDataSetChanged();
    }
    
    private void validateInputs() {
        boolean isValid = true;
        if (etLoanAmount.getText().toString().isEmpty()) {
            etLoanAmount.setError("Loan amount is required");
            isValid = false;
        }
        if (etInterestRate.getText().toString().isEmpty()) {
            etInterestRate.setError("Interest rate is required");
            isValid = false;
        }
        if (etLoanTerm.getText().toString().isEmpty()) {
            etLoanTerm.setError("Loan term is required");
            isValid = false;
        }
        if (isValid) {
            calculateLoan();
        }
    }
    
    private void calculateLoan() {
        try {
            double loanAmount = Double.parseDouble(etLoanAmount.getText().toString());
            double interestRate = Double.parseDouble(etInterestRate.getText().toString());
            int loanTerm = Integer.parseInt(etLoanTerm.getText().toString());
            
            double monthlyInterestRate = (interestRate / 100) / 12;
            double totalPayments = loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTerm * 12)) / (Math.pow(1 + monthlyInterestRate, loanTerm * 12) - 1);
            double totalInterest = totalPayments * loanTerm * 12 - loanAmount;
            
            DecimalFormat df = new DecimalFormat("#,##0.00");
            tvMonthlyPayment.setText("Monthly Payment: Rs. " + df.format(totalPayments));
            tvTotalPayment.setText("Total Payment: Rs. " + df.format(totalPayments * loanTerm * 12));
            resultsContainer.setVisibility(View.VISIBLE);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for loan amount, interest rate, and term.", Toast.LENGTH_SHORT).show();
            resultsContainer.setVisibility(View.GONE);
        }
    }
    
    private void showCalculator() {
        // Focus on calculator section
        etLoanAmount.requestFocus();
        Toast.makeText(this, "Use the loan calculator below", Toast.LENGTH_SHORT).show();
    }
    
    private void showGrantsInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Available Grants");
        
        String[] grants = {
            "PM Kisan Samman Nidhi (Rs. 6,000 per year for small and marginal farmers)",
            "Pradhan Mantri Fasal Bima Yojana (Crop insurance for 50% of the premium)",
            "Pradhan Mantri Krishi Sinchai Yojana (Integrated Farming System)",
            "National Mission for Sustainable Agriculture (NMSA)",
            "Rashtriya Krishi Vikas Yojana (RKVY)"
        };
        
        builder.setItems(grants, (dialog, which) -> {
            String selectedGrant = grants[which];
            Toast.makeText(this, "You selected: " + selectedGrant, Toast.LENGTH_SHORT).show();
        });
        
        builder.setPositiveButton("Close", null);
        builder.show();
    }
    
    private void contactAdvisor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Financial Advisor");
        builder.setMessage("For personalized financial advice, please contact:\n\nName: John Doe\nPhone: +91 9876543210\nEmail: info@example.com\n\nOr visit your nearest bank branch.");
        
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    // Financial Program Data Class
    public static class FinancialProgram {
        public String name;
        public String description;
        public String type;
        public String amount;
        public String interest;
        public String term;
        
        public FinancialProgram(String name, String description, String type, String amount, String interest, String term) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.amount = amount;
            this.interest = interest;
            this.term = term;
        }
    }
    
    // Program Adapter
    private class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {
        private List<FinancialProgram> programs;
        
        public ProgramAdapter(List<FinancialProgram> programs) {
            this.programs = programs;
        }
        
        @Override
        public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_financial_program, parent, false);
            return new ProgramViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(ProgramViewHolder holder, int position) {
            FinancialProgram program = programs.get(position);
            
            holder.programName.setText(program.name);
            holder.programDescription.setText(program.description);
            holder.programType.setText("Type: " + program.type);
            holder.programAmount.setText("Amount: " + program.amount);
            holder.programInterest.setText("Interest: " + program.interest);
            holder.programTerm.setText("Term: " + program.term);
        }
        
        @Override
        public int getItemCount() {
            return programs.size();
        }
        
        class ProgramViewHolder extends RecyclerView.ViewHolder {
            TextView programName, programDescription, programType, programAmount, programInterest, programTerm;
            
            public ProgramViewHolder(View itemView) {
                super(itemView);
                programName = itemView.findViewById(R.id.programName);
                programDescription = itemView.findViewById(R.id.programDescription);
                programType = itemView.findViewById(R.id.programType);
                programAmount = itemView.findViewById(R.id.programAmount);
                programInterest = itemView.findViewById(R.id.programInterest);
                programTerm = itemView.findViewById(R.id.programTerm);
            }
        }
    }
}

