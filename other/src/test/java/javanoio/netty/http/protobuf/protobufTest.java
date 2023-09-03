package javanoio.netty.http.protobuf;

/**
 * @ClassName test
 * @Description TODO
 * @Author zzk
 * @Date 2023/8/14 22:00
 **/

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;
public class protobufTest {
    @Test
    public void testN() throws InvalidProtocolBufferException {
        AddressBookProtos.Person.Builder builder = AddressBookProtos.Person.newBuilder();
        builder.setId(1);
        builder.setName("jihite");
        builder.setEmail("jihite@jihite.com");

        AddressBookProtos.Person person = builder.build();
        System.out.println("before:" + person);

        System.out.println("===Person Byte:");
        for (byte b : person.toByteArray()) {
            System.out.print(b);
        }
        System.out.println("================");

        byte[] byteArray = person.toByteArray();
        AddressBookProtos.Person p2 = AddressBookProtos.Person.parseFrom(byteArray);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());

    }
}
