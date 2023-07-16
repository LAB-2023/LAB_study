package core.hello;

import core.hello.member.Grade;
import core.hello.member.Member;
import core.hello.member.MemberService;
import core.hello.member.MemberServiceImpl;
import core.hello.order.Order;
import core.hello.order.OrderService;
import core.hello.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);
        System.out.println("order = " + order.calculatePrice());
    }
}
