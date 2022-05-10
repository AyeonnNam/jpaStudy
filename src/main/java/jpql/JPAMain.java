package jpql;

import javax.persistence.*;
import java.util.List;

public class JPAMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hi");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Member member = new Member();
            member.setName("남아연");
            member.setAge(10);
            em.persist(member);


//            Member member2 = new Member();
//            member2.setName("김준수");
//            member2.setAge(10);
//            em.persist(member2);
//
//            //TypeQuery: 반환 타입이 명확할때 사용
//            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
//            TypedQuery<String> stringTypedQuery = em.createQuery("select m.name from Member m", String.class);
//
//            //값을 두개 이상 가지고 올때, 예를 들어 컬렉션값
//            List<Member> resultList = query.getResultList();
//            for (Member memberList : resultList) {
//                System.out.println("memberList = " + memberList.getName());
//            }
//
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m where m.age= 10 ", Member.class);
//            Member singleResult = query1.getSingleResult();
//            System.out.println("singleResult = " + singleResult.getName());
//
//            //값이 없는 경우 NoResultException이 뜬다고 했지만 Exception이 안뜬다....................
//            TypedQuery<String> query3 = em.createQuery("select m from Member m where m.name='akd'", String.class);
//            String singleResult1 = query3.getSingleResult();
//            System.out.println("singleResult1 = " + singleResult1);
//
//            System.out.println("----------------------------------------------------------");

                        //Query: 반환 타입이 명확하지 않을때
//            Query query2 = em.createQuery("select m.name, m.age from Member m");
//
//                //파라미터 바인딩을 하는 이유는???
//            Member singleResult2 = em.createQuery("select m from Member m where m.name= :name", Member.class)
//                    .setParameter("name", "남아연")
//                    .getSingleResult();
//
//            System.out.println("singleResult2 = " + singleResult2.getName());

            //프로젝션
            /* SELECT 절에 조회할 대상을 지정하는 것
            *  프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(슷자, 문자등 기본 데이터 타입)
            *  SELECT m FROM Member m --> 엔티티 프로젝션
            *   SELECT m.team FROM Member m --> 엔티티 프로젝션
            *  SELECT m.address FROM Member m --> 임베디드 타입 프로젝션
            *  SELECT m.username, m.age FROM Member m --> 스칼라 타입 프로젝션
            *  DISTNICT로 중복 제거
            * */



            em.flush();
            em.clear();

            //영속성컨텍스트가 관리해주고 있음
            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();

            Member member1 = result.get(0);
            member1.setAge(30);

            //멤버와 연관관계인 팀도 조회 가능, 대신 sql과 최대한 비슷하게 써주는 게 좋다 ex) join
            List<Team> resultList = em.createQuery("select t from Member m join m.team t ", Team.class).getResultList();

            //임베디드 프로젝션
            List<Address> resultList1 = em.createQuery("select o.address from Order o", Address.class).getResultList();

            //스칼라 프로젝션
            Object singleResult = em.createQuery("select distinct m.name, m.age from Member m").getSingleResult();

            List resultList2 = em.createQuery("select m.name, m.age from Member m").getResultList();

            Object o = resultList2.get(0);
            Object[] result2 = (Object[]) o;
            System.out.println("name = " + result2[0]);
            System.out.println("age = " + result2[1]);

            List<Object[]> resultList3 = em.createQuery("select m.name, m.age from Member m").getResultList();

            Object[] o1 = resultList3.get(0);
            System.out.println("name = " + o1[0]);
            System.out.println("age = " + o1[1]);

            //프로젝션 - 여러 값 조회
            /* SELECT m.username, m.age FROM Member m
             *   1. Query 타입으로 조회
             *   2. Object[] 타입으로 조회
             *   3. new 명령어로 조회
             *   - 단순 값을 DTO로 바로 조회
             *   SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m
             *   패키지 명을 포함한 전체 클래스 명 입력
             *   순서와 타입이 일치하는 생성자 필요
             * */


            tx.commit();
    }catch(Exception e ){
        tx.rollback();

    }finally {
        em.close();
    }emf.close();

}


}
