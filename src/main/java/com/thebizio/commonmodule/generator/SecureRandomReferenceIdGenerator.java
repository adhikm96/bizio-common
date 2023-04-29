package com.thebizio.commonmodule.generator;

import com.thebizio.commonmodule.entity.*;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.tuple.ValueGenerator;

import java.security.SecureRandom;
import java.util.Optional;

public class SecureRandomReferenceIdGenerator implements ValueGenerator<String> {

    private String prefix;

    private static final String CHAR_UPPER = "ABCDEFGHHJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER = "0123456789";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();

    @Override
    public String generateValue(Session session, Object obj) {
        String randomNumber = generateRandomString(5);
        if (obj.getClass().getSimpleName().equals("Order")){
            prefix = "ORD-";

            Query<Order> query = session.createQuery("from Order o where o.refNo=:rn", Order.class);
            query.setParameter("rn", prefix+randomNumber);

            Optional<Order> order = query.setHibernateFlushMode(FlushMode.COMMIT).uniqueResultOptional();

            if (order.isPresent()){
                return generateValue(session,obj);
            }else {
                return prefix+randomNumber;
            }
        } else if (obj.getClass().getSimpleName().equals("Account")) {
            prefix = "ACC-";

            Query<Account> query = session.createQuery("from Account a where a.code=:rn", Account.class);
            query.setParameter("rn", prefix+randomNumber);

            Optional<Account> account = query.setHibernateFlushMode(FlushMode.COMMIT).uniqueResultOptional();

            if (account.isPresent()){
                return generateValue(session,obj);
            }else {
                return prefix+randomNumber;
            }
        }else if (obj.getClass().getSimpleName().equals("Organization")) {
            prefix = "ORG-";

            Query<Organization> query = session.createQuery("from Organization o where o.code=:rn", Organization.class);
            query.setParameter("rn", prefix + randomNumber);

            Optional<Organization> organization = query.setHibernateFlushMode(FlushMode.COMMIT).uniqueResultOptional();

            if (organization.isPresent()) {
                return generateValue(session, obj);
            } else {
                return prefix + randomNumber;
            }
        }else  if (obj.getClass().getSimpleName().equals("Invoice")){
            prefix = "INV-";

            Query<Invoice> query = session.createQuery("from Invoice i where i.refNo=:rn", Invoice.class);
            query.setParameter("rn", prefix+randomNumber);

            Optional<Invoice> invoice = query.setHibernateFlushMode(FlushMode.COMMIT).uniqueResultOptional();

            if (invoice.isPresent()){
                return generateValue(session,obj);
            }else {
                return prefix+randomNumber;
            }
        }else  if (obj.getClass().getSimpleName().equals("Payment")){
            prefix = "PMT-";

            Query<Payment> query = session.createQuery("from Payment p where p.refNo=:rn", Payment.class);
            query.setParameter("rn", prefix+randomNumber);

            Optional<Payment> payment = query.setHibernateFlushMode(FlushMode.COMMIT).uniqueResultOptional();

            if (payment.isPresent()){
                return generateValue(session,obj);
            }else {
                return prefix+randomNumber;
            }
        }
        else if (obj instanceof IRandomGeneratorField) {
            prefix = String.valueOf(obj.getClass().getSimpleName().subSequence(0,3)).toUpperCase() + "-"; // taking first 3 chars as uppercase
            IRandomGeneratorField field = (IRandomGeneratorField) obj;
            Query<?> query = session.createQuery("from "+ obj.getClass().getSimpleName() +" o where o."+ field.getRandomGeneratorField() +"=:rn", obj.getClass());
            query.setParameter("rn", prefix+randomNumber);

            if (query.setHibernateFlushMode(FlushMode.COMMIT).uniqueResultOptional().isPresent()){
                return generateValue(session,obj);
            }
            return prefix+randomNumber;
        }
        return null;
    }

    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }
}
