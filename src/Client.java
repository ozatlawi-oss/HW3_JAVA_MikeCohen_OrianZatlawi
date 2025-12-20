public class Client {
    private String username;
    private String password;
    private String status;
    private int yearsInRuppin;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getYearsInRuppin() {
        return yearsInRuppin;
    }
    public void setYearsInRuppin(int yearsInRuppin) {
        this.yearsInRuppin = yearsInRuppin;
    }
    public Client(String username, String password, String status, int yearsInRuppin) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.yearsInRuppin = yearsInRuppin;
    }

    @Override
    public boolean equals(Object other) {
        if(this==other)
            return true;
        if(other.getClass()!=this.getClass() || other==null){
            return false;
        }
        return this.yearsInRuppin == ((Client) other).yearsInRuppin &&
                this.status == ((Client) other).status &&
                this.username.equals(((Client) other).username) &&
                this.password.equals(((Client) other).password);

    }

}
