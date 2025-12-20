import java.util.ArrayList;
import java.util.List;

public class RuppinRegistrationProtocol {
    List<Client> clientState;
    String userName;
    String password;
    String status;
    int yearsIn;
    boolean registered = false;

    private static final int WAITING = 0;
    private static final int AWATING_YES_NO = 1;
    private static final int AWATING_NEW_USERNAME = 1;
    private static final int CHECK_USER_NAME = 2;
    private static final int AWATING_NEW_PASSWORD = 4;
    private static final int CHECK_USER_PASSWORD = 5;
    private static final int AWATING_ACADEMIC_STATUS = 6;
    private static final int CHECK_ACADEMIC_STATUS = 7;
    private static final int GET_YEARS_IN=8;
    private static final int FINISHED=9;
int state = WAITING;
public RuppinRegistrationProtocol(List<Client> clientState){
    this.clientState=clientState;

}

    public String flowManager(String Input){
    String output=null;
        if(state==WAITING){
            output="Do you want to register? (yes/no)";
            state=AWATING_YES_NO;

        }
        else if(state==AWATING_YES_NO){
            if(Input.equals("yes")){
                state=AWATING_NEW_USERNAME;
                output="Enter a username:";
            }
            else if(Input.equals("no")){
                //משתמש קיים
            }
        }
        else if(state==AWATING_NEW_USERNAME){
            state=CHECK_USER_NAME;
            if(!IsUserNameTaken(Input)){
                userName=Input;
                state=AWATING_NEW_PASSWORD;
                output="OK.Enter a Strong Password:";
            }
            else{
                output="Username already exists.";
            }
        }
        else if(state==AWATING_NEW_PASSWORD){
            state=CHECK_USER_PASSWORD;
            if(IsPasswordFit(Input)){
                output="Password Accepted."+"\n"+"What is your academic status? (student/teacher/other)";
                this.password=Input;
                state=AWATING_ACADEMIC_STATUS;
            }
            else{
                output="Password Not Accepted. Must be in len of 9 and Contain 1 Upper And lower case letters and 1 number 0-9";
            }

        }
        else if(state==AWATING_ACADEMIC_STATUS){
            state=CHECK_ACADEMIC_STATUS;
            if(IsAcademicStatus(Input)){
                output="How many years have you been at Ruppin?";
                status=Input;
                state=GET_YEARS_IN;
            }
            else{
                output="Academic Status Not Accepted. Enter ONLY student/teacher/other!!";
            }

        }
        else if(state==GET_YEARS_IN){
            state=FINISHED;
            yearsIn=Integer.parseInt(Input);
            output="Server: Registration complete";
            clientState.add(new Client(userName,password,status,yearsIn));
        }
        return output;
    }






    private  boolean IsUserNameTaken(String userName){
    for(Client c:clientState){
        if(c.getUsername().equals(userName)){
            return true;
        }

    }
    return false;
    }
    private boolean IsPasswordFit(String pass){
    return pass.length()>=9 && pass.matches(".*[A-Z].*")&&pass.matches(".*[a-z].*")&&pass.matches(".*[0-9].*");
    }
    private boolean IsAcademicStatus(String status){
    return status.equalsIgnoreCase("student")  || status.equalsIgnoreCase("teacher") || status.equalsIgnoreCase("other");

    }

}
