package eapli.shodrone.showProposal.domain.proposalFields;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Random;

@Embeddable
public class DownLoadCode implements ValueObject, Comparable<DownLoadCode> {

    private String code;


    private DownLoadCode(String code){
        this.code = code;
    }

    public DownLoadCode() {
        // for ORM;
    }

    public static DownLoadCode generateNewCode(long receivedID){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890123456789";
        Random rd = new Random();

        StringBuilder code =  new StringBuilder();
        code.append("P");

        for(int i = 0; i < 7; i++){
            char letter = abc.charAt(rd.nextInt(abc.length()));
            code.append(letter);
        }
        code.append(String.format("%02d", receivedID));
        return new DownLoadCode(code.toString());
    }

    public String value(){
        return code;
    }

    @Override
    public int compareTo(DownLoadCode o) {
        return code.compareTo(o.code);
    }
}
