package bpm.camunda.engine.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.logging.Logger;


@Component(value = "generateEmi")
public class GenerateEmi implements JavaDelegate {

    Logger logger = Logger.getLogger(GenerateEmi.class.getName());
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        logger.info("--Calling Generate EMI  Delegate--");

        generateEmi(delegateExecution);

        logger.info("--Ending Generate EMI  Delegate--");

    }


    public void generateEmi(DelegateExecution delegateExecution){

        Double salary = (Double) delegateExecution.getVariable("salary");

        Double existingLoanAMount = (Double) delegateExecution.getVariable("existingLoan");

        Double newLoanAMount = (Double) delegateExecution.getVariable("newLoanAmount");

        Double rateOfInterest = (Double) delegateExecution.getVariable("rateOfInterest");

        Double ternure = (Double) delegateExecution.getVariable("ternure");

        float emiPerMonth;
        double  finalRemainingAmount = 0;
        if(salary != null){
            Double doubleSalary = salary * 10;
            if(existingLoanAMount != null && existingLoanAMount != 0 ){
                finalRemainingAmount = doubleSalary - existingLoanAMount;
            }else {
                finalRemainingAmount = doubleSalary;
            }
            if(  newLoanAMount < finalRemainingAmount ){
                 emiPerMonth = emiCalculator(newLoanAMount.floatValue(),rateOfInterest.floatValue(),ternure.floatValue());
                logger.warning("You are eligible fo the Loan Request for {} "+ newLoanAMount +" and EMI is per month ::"+emiPerMonth );
                delegateExecution.setVariable("emiPerMonth",emiPerMonth);
            }else{
                logger.warning("You loan amount should be less then Remaining Amount is {} "+ finalRemainingAmount );
            }
        }
    }

    static float emiCalculator(float p,float r, float t)
    {
        float emi;
        r = r / (12 * 100); // one month interest
        t = t * 12; // one month period
        emi = (p * r * (float)Math.pow(1 + r, t))
                / (float)(Math.pow(1 + r, t) - 1);
        return (emi);
    }
}
