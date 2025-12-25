import java.util.List;

public class RuppinRegistrationProtocol {
    List<Client> clientState;
    String userName;
    String password;
    String status;
    int yearsIn;
    int IndexOfUser;
    boolean registered = false;
    boolean updateDetails = false;
    boolean completed = false;

    public boolean getCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {}

    private static final int WAITING = 0;
    private static final int AWATING_YES_NO = 1;
    private static final int AWATING_NEW_USERNAME = 2;
    private static final int AWATING_NEW_PASSWORD = 3;
    private static final int AWATING_ACADEMIC_STATUS = 4;
    private static final int GET_YEARS_IN = 5;
    private static final int FINISHED = 9;
    private static final int AWAITING_EXISTING_USERNAME = 11;
    private static final int AWAITING_EXISTING_PASSWORD = 12;
    private static final int CHECK_UPDATE_INFO = 13;
    private static final int ASK_UPDATE_USERNAME = 14;
    private static final int UPDATE_USERNAME = 15;
    private static final int ASK_UPDATE_PASSWORD = 16;
    private static final int UPDATE_PASSWORD = 17;
    private static final int ASK_UPDATE_YEARS_IN = 18;
    private static final int UPDATE_YEARS_IN = 19;
    private static final int FINAL_UPDATE_MSG = 20;

    int state = WAITING;

    public RuppinRegistrationProtocol(List<Client> clientState) {
        this.clientState = clientState;
    }

    public String flowManager(String Input) {
        String output = null;
        if (!updateDetails) {
            if (state == WAITING) {
                output = "Do you want to register? (yes/no)";
                state = AWATING_YES_NO;
            } else if (state == AWATING_YES_NO) {
                if (Input.equalsIgnoreCase("yes")) {
                    state = AWATING_NEW_USERNAME;
                    output = "Enter a username:";
                } else if (Input.equalsIgnoreCase("no")) {
                    state = AWAITING_EXISTING_USERNAME;
                    output = "Username:";
                }
            } else if (state == AWATING_NEW_USERNAME) {
                if (!IsUserNameTaken(Input)) {
                    userName = Input;
                    state = AWATING_NEW_PASSWORD;
                    output = "Checking name... OK. Enter a strong password:";
                } else {
                    output = "Name not OK. Username exists. Choose a different name:";
                }
            } else if (state == AWATING_NEW_PASSWORD) {
                if (IsPasswordFit(Input)) {
                    this.password = Input;
                    state = AWATING_ACADEMIC_STATUS;
                    output = "Password accepted. What is your academic status? (student/teacher/other)";
                } else {
                    output = "Password Not Accepted. Must be in len of 9 and Contain 1 Upper And lower case letters and 1 number 0-9";
                }
            } else if (state == AWATING_ACADEMIC_STATUS) {
                if (IsAcademicStatus(Input)) {
                    status = Input;
                    state = GET_YEARS_IN;
                    output = "How many years have you been at Ruppin?";
                } else {
                    output = "Academic Status Not Accepted. Enter ONLY student/teacher/other!!";
                }
            } else if (state == GET_YEARS_IN) {
                try {
                    yearsIn = Integer.parseInt(Input);
                    output = "Registration complete.";
                    synchronized(clientState) {
                        clientState.add(new Client(userName, password, status, yearsIn));
                        if(clientState.size()%3==0){//backup to csv
                            RuppinServer.saveToCSV(clientState);

                        }
                    }
                    completed = true;
                } catch (NumberFormatException e) {
                    output = "Please enter a valid number for years:";
                }
            } else if (state == AWAITING_EXISTING_USERNAME) {
                if (IsUserNameTaken(Input)) {
                    userName = Input;
                    state = AWAITING_EXISTING_PASSWORD;
                    output = "Password:";
                } else {
                    output = "Username Not Found. Username:";
                }
            } else if (state == AWAITING_EXISTING_PASSWORD) {
                if (CheckPassword(Input, IndexOfUser)) {
                    Client c = clientState.get(IndexOfUser);
                    output = "Welcome back, " + userName + ". Last time you defined yourself as " + c.getStatus() + " for " + c.getYearsInRuppin() + " years. Do you want to update your information? (yes/no)";
                    state = CHECK_UPDATE_INFO;
                } else {
                    output = "Password Incorrect. Password:";
                }
            } else if (state == CHECK_UPDATE_INFO) {
                if (Input.equalsIgnoreCase("yes")) {
                    updateDetails = true;
                    state = ASK_UPDATE_USERNAME;
                    return flowManager(null);
                } else {
                    output = "Bye.";
                    completed = true;
                }
            }
            return output;
        }

        if (state == ASK_UPDATE_USERNAME) {
            output = "Do you want to change your username? (yes/no)";
            state = UPDATE_USERNAME;
        } else if (state == UPDATE_USERNAME) {
            if (Input.equalsIgnoreCase("yes")) {
                output = "Enter new username:";
                state = ASK_UPDATE_PASSWORD;
            } else {
                state = ASK_UPDATE_PASSWORD;
                return flowManager(null);
            }
        } else if (state == ASK_UPDATE_PASSWORD) {
            if (Input != null) {
                clientState.get(IndexOfUser).setUsername(Input);
                output = "Username updated successfully. ";
            } else {
                output = "";
            }
            output += "Do you want to change your password? (yes/no)";
            state = UPDATE_PASSWORD;
        } else if (state == UPDATE_PASSWORD) {
            if (Input.equalsIgnoreCase("yes")) {
                output = "Enter new password:";
                state = ASK_UPDATE_YEARS_IN;
            } else {
                state = ASK_UPDATE_YEARS_IN;
                return flowManager(null);
            }
        } else if (state == ASK_UPDATE_YEARS_IN) {
            if (Input != null) {
                clientState.get(IndexOfUser).setPassword(Input);
                output = "Password updated successfully. ";
            } else {
                output = "";
            }
            output += "Do you want to update your years of study? (yes/no)";
            state = UPDATE_YEARS_IN;
        } else if (state == UPDATE_YEARS_IN) {
            if (Input.equalsIgnoreCase("yes")) {
                output = "Enter number of years:";
                state = FINAL_UPDATE_MSG;
            } else {
                state = FINAL_UPDATE_MSG;
                return flowManager(null);
            }
        } else if (state == FINAL_UPDATE_MSG) {
            if (Input != null) {
                clientState.get(IndexOfUser).setYearsInRuppin(Integer.parseInt(Input));
                output = "Years of study updated successfully. ";
            } else {
                output = "";
            }
            output += "Thanks. Your information has been updated.";
            completed = true;
        }
        return output;
    }

    private boolean IsUserNameTaken(String userName) {
        synchronized(clientState) {
            for (Client c : clientState) {
                if (c.getUsername().equals(userName)) {
                    IndexOfUser = clientState.indexOf(c);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean IsPasswordFit(String pass) {
        return pass.length() >= 9 && pass.matches(".*[A-Z].*") && pass.matches(".*[a-z].*") && pass.matches(".*[0-9].*");
    }

    private boolean IsAcademicStatus(String status) {
        return status.equalsIgnoreCase("student") || status.equalsIgnoreCase("teacher") || status.equalsIgnoreCase("other");
    }

    private boolean CheckPassword(String password, int IndexOfUser) {
        return password.equals(clientState.get(IndexOfUser).getPassword());
    }
}