package com.mergyping.mbticontroller;

import com.mergyping.model.dto.MbtiTester;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/*")
public class MbtiController {

    // 의존성 주입을 통해? 만들어야 dto 객체가 합쳐지는 듯?
    @Autowired
    private MbtiTester mbtiTester; // 생각해보니 componentscan을써도 dto에 bean이라고 지정하지 않았었기에 지정해주었다.

    @GetMapping("/mbti1")
    public String mbti1(Model model, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 이거없으면 500에러 뜸 -> 왜냐면 안가져와서 mbtiTester가 null이니까?? 모르게따
        // 그리고 같은 mbti1이고 getmapping과 postmapping에서 각 위치에서 필요할때마다 세션 불러오고 저장하고 해야함 별개라고 생각하면서 필요한 구간에 알맞게 넣어야할듯?
            model.addAttribute("mbtiMessage", "담력시험을 하기로 하여 폐가에서 하룻밤을 보내기로 했다.<br>"
                    + "그러던 중 갑작스러운 인기척에 잠에서 깼다.<br>"
                    + "어떻게 할까?<br>"
                    + "1. 그 자리에 숨어서 상황을 지켜본다.<br>"
                    + "2. 무기로 쓸만한 물건을 찾아 들고 나가서 확인한다.<br>"
                    + "당신의 선택은 ? ");
// 여기서는 session이 필요없을거라 생각했는데 없으니까 문제가 발생했다. -> 값이 제대로 쌓이지 않는 문제?
        if (mbtiTester == null) {
//            MbtiTester mbtiTester = new MbtiTester(); 이렇게 선언하면 안되는것같다 -> 이건 변수를 새롭게 생성하는 것이기 때문에 기존 변수가 이상해지는 것 같다.
            mbtiTester = new MbtiTester(); // 이렇게 해야 기존 객체를 이 변수에 담는것이고
            session.setAttribute("mbtiTester", mbtiTester);// 생성을 해줘야하는데 처음엔 null일 것이므로 null이면 새로운 객체를 생성?
        }

            return "mbti1";
    }

    @PostMapping("/mbti1")
    public String mbti1Answer(@RequestParam int answer, HttpSession session) { // @ModelAttribute MbtiTester mbtiTester 이건 빼야 할듯? 내가 session에 담아서 사용하고 있어서 겹침
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        // 자꾸 안돼서 생각해보니 html 쪽에서 action을 사용하지 않아서 발생하는 문제였다.
        return "redirect:/mbti2";
    }

    @GetMapping("/mbti2")
    public String mbti2(Model model, HttpSession session) {
        // 일단 아까 mbti1에서 만든 세션에서 저장한 객체를 불러와서 다시 가지고 있어야 할 것 같아서 만들었다. 아까는 set으로 세션에 저장을 했으니 이번엔 get으로 꺼내와야겠지?
        // 라고 생각했으나 이젠 여기선 필요가 없다.
        model.addAttribute("mbtiMessage", "집을 가는 길에 항상 지나가야하는 굴다리(OR 골목)가 있다.<br>"
                + "오늘은 시간이 늦어 가로등도 꺼지고 불이 없는 상황이다.<br>"
                + "이 길로 가지 않으면 10분을 돌아가야하는데 중간에 사람형체인지 모를 이상한 실루엣이 흔들거리고 있다.<br>"
                + "돌아가야할까? 그냥 지나갈까?<br>"
                + "1. 운동이나 할겸 돌아간다.<br>"
                + "2. 무서울 것 없다. 그냥 간다.<br>"
                + "당신의 선택은 ? ");
//        model.addAttribute("mbtiTester", mbtiTester); // 이렇게하면 이전 Session에서 가지고 있던 값을 여기서 계승해서 사용하는게 맞는건지 정확히 모르겠다. -> 필요없는 것 같아 주석처리
        return "mbti2";
    }

    @PostMapping("/mbti2")
    public String mbti2Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
//        System.out.println(mbtiTester); // 되는지 체크해보자 -> 일단 자꾸 초기화 되고 쌓이기 때문에 이 값이 유지가 되지 아니함 -> 전체변경후 유지완료 // 세션에 현재까지 문제없음
        return "redirect:/mbti3";
    }

    @GetMapping("/mbti3")
    public String mbti3(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "산에서 길을 잃고 휴대폰 배터리까지 떨어져 조난당했다.<br>"
                + "야생동물이 나올 걱정은 없는 산이지만 어두워져 당장 좋지 않은 상황이다.<br>"
                + "최근 심장마비로 알수없이 죽은 사람들이 있어 불안한데 근처에서 소름끼치는 흐느끼는 소리가 들린다.<br>"
                + "다행히 조난신호를 보내놓아 구조대가 올 가능성이 있지만 당장은 아닌 상황이다. 어떡할까?<br>"
                + "1. 소리가 나는 반대 방향으로 가거나 조난당한 위치에서 그대로 구조를 기다린다.<br>"
                + "2. 소리가 나는 곳으로 가본다.<br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
                                                                 //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti3";
    }

    @PostMapping("/mbti3")
    public String mbti3Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti4";
    }

    @GetMapping("/mbti4")
    public String mbti4(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "외국에 놀러갔는데 옥수수 밭을 탐방하던 도중 옥수수 밭에서 조난당했다.<br>"
                + "한도 끝도 없는 옥수수 밭에서 조난당했다고 신호는 보낸 상황인데 근처에서 신호를 보냄과 동시에 나한테 무언가 다가오는 소리가 들린다.<br>"
                + "구조대가 아닌 것은 확실하지만 어떻게 할까?<br>"
                + "1. 살짝 숨어서 무엇이 오는지 지켜본 뒤에 행동한다.<br>"
                + "2. 누군지 불러본다.<br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti4";
    }

    @PostMapping("/mbti4")
    public String mbti4Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti5";
    }

    @GetMapping("/mbti5")
    public String mbti5(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "외국 여행 중 산에서 삐쩍 마른 곰을 발견했다.<br>"
                + "아직 곰을 날 확인한 것은 아니지만 나를 발견하면 미친듯이 달려들 것 같은 예감이 든다<br>"
                + "다행히 호신용 총이 있지만 단 한발밖에 없다.<br>"
                + "곰이 있는 곳으로 지나가지 않으면 하산할 수 없어 어두워지는 이 상황에 한시가 급하다.<br>"
                + "어떻게 해야할까?<br>"
                + "1. 조용히 숨을 죽인채 지나가길 기다린다. <br>"
                + "2. 곰과 싸운다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti5";
    }

    @PostMapping("/mbti5")
    public String mbti5Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti6";
    }

    @GetMapping("/mbti6")
    public String mbti6(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "납치를 당해 위험한 상황이다.<br>"
                + "다행히 풀려있던 밧줄이 헐거워 풀 수 있었고 옆에서 무기를 찾을 수 있었다.<br>"
                + "현재 바깥이 조용하고 새소리가 나는 것으로 보아 낮인 것 같다는 판단을 할 수 있었고<br>"
                + "탈출시도를 하거나 여기서 기다렸다가 납치범이 돌아오면 습격을 할 수 있을 것 같다.<br>"
                + "어떻게 해야할까?<br>"
                + "1. 탈출을 시도한다. <br>"
                + "2. 아직 있을지 모른다. 기다렸다가 기습한다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti6";
    }

    @PostMapping("/mbti6")
    public String mbti6Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti7";
    }

    @GetMapping("/mbti7")
    public String mbti7(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "시골이지만 드디어 내 집을 마련했다.<br>"
                + "2층이나 되는 굉장히 넓은 집이다.<br>"
                + "혼자 살고 있지만 나에게도 가족이 생기면 굉장히 소중한 집이 될 것 같다.<br>"
                + "지금 시간은 오후 11시, 그 때 2층에서 이상한 소리가 들렸고 잘못들었다고 하기엔 굉장히 소름끼치는 소리었다.<br>"
                + "어떻게 할까?<br>"
                + "1. 설마 집에 누군가 들어왔겠어? 무기가 될만한 것을 찾아서 위로 올라가서 확인한다. <br>"
                + "2. 아무리 생각해도 이건 무언가 침입한 것이다. 밖으로 도주한 뒤 안전한 곳에서 경찰에게 도움을 요청한다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti7";
    }

    @PostMapping("/mbti7")
    public String mbti7Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti8";
    }

    @GetMapping("/mbti8")
    public String mbti8(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "나는 산행을 좋아하여 국내 산행은 모두 마치고 외국에 있는 모든 산행을 하기 마음먹어 여행을 다니고 있다.<br>"
                + "오늘은 산이 너무 험해 길을 잃고 말았다.<br>"
                + "그러나 다행히 산 중턱에서 우거진 숲속에 둘러쌓여진 오두막을 발견했고 겉으로 보기에는 최근까지 누군가가 살았던 흔적이 있었다.<br>"
                + "불안한 점은 일반적인 오두막이라기보다는 무언가를 잡는 곳이었다는 느낌이 강하고 일부러 이 안쪽에 지어놓았다는 직감이 들었다.<br>"
                + "해가 지기 직전이라서 그냥 갔다가는 야생동물 등의 위험에 노출될 것이다.<br>"
                + "어떻게 할까? <br>"
                + "1. 그냥 일을 위해 사용하던 오두막일 것이다. 입구에서 주인을 기다리거나 오두막에 들어가본다. (숨어서 지켜볼 수 있는 방법도 있다.) <br>"
                + "2. 무슨 일이 있을지 모르는데 해가 지더라도 더 길을 찾아 나아가본다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti8";
    }

    @PostMapping("/mbti8")
    public String mbti8Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti9";
    }

    @GetMapping("/mbti9")
    public String mbti9(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "뒤에서 누군가 쫓아오고 있다.<br>"
                + "뛰어서 도망가고 있지만 점점 숨이 차는 상황이다.<br>"
                + "측면 모퉁이를 돌았을 때 내가 할 행동은?<br>"
                + "어떻게 할까 ? <br>"
                + "1. 모퉁이의 숨을 공간을 찾아 재빨리 들어간다.<br>"
                + "2. 모퉁이를 돌아 쓰레기 더미에서 무기를 찾아 추격자를 기다렸다가 습격한다.<br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti9";
    }

    @PostMapping("/mbti9")
    public String mbti9Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setCB(mbtiTester.getCB()+1);
        } else if (answer == 2) {
            mbtiTester.setCB(mbtiTester.getCB()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti10";
    }

    @GetMapping("/mbti10")
    public String mbti10(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "1000평짜리 5층 집에서 일주일만 이것과 함께 살면 100억을 준다고 한다.<br>"
                + "집이 매우 넓어서 충분히 숨을 수 있을 것 같다.<br>"
                + "어떤 것과 같이 살까?<br>"
                + "어떻게 할까 ? <br>"
                + "1. 귀신 <br>"
                + "2. 살인마 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti10";
    }

    @PostMapping("/mbti10")
    public String mbti10Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti11";
    }

    @GetMapping("/mbti11")
    public String mbti11(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "사람이 귀신보다 더 무섭다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti11";
    }

    @PostMapping("/mbti11")
    public String mbti11Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti12";
    }

    @GetMapping("/mbti12")
    public String mbti12(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "집 침대밑이나 세탁기 안에 숨어있다면 더 무서운건 무엇일까? <br>"
                + "1. 사람 <br>"
                + "2. 괴물 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti12";
    }

    @PostMapping("/mbti12")
    public String mbti12Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti13";
    }

    @GetMapping("/mbti13")
    public String mbti13(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "일주일동안 같은 꿈을 꿔야 하는데 선택해야 한다면? <br>"
                + "1. 꿈에서 칼든 살인마가 쫓아오는 꿈 <br>"
                + "2. 물구나무 선 귀신이 팔로 쫓아오는 꿈 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti13";
    }

    @PostMapping("/mbti13")
    public String mbti13Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti14";
    }

    @GetMapping("/mbti14")
    public String mbti14(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "담력시험을 하러 폐가에 갔을 때 더 무서운 것은? <br>"
                + "1. 피와 사람의 흔적 <br>"
                + "2. 혼자서 움직이는 의자와 그네 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti14";
    }

    @PostMapping("/mbti14")
    public String mbti14Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti15";
    }

    @GetMapping("/mbti15")
    public String mbti15(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "집에 귀가했을 때 <br>"
                + "내 방으로 들어왔을 때 침대 밑의 무언가와 눈이 마주쳤다. <br>"
                + "그건 무엇이었을까? <br>"
                + "1. 사람 <br>"
                + "2. 귀신 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti15";
    }

    @PostMapping("/mbti15")
    public String mbti15Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti16";
    }

    @GetMapping("/mbti16")
    public String mbti16(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "하나를 선택한다면? <br>"
                + "1. 귀신이랑 하룻동안 엘레베이터에 갇혀서 살아남기 <br>"
                + "2. 살인마와 20층 건물 비상구에서 살아남기 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti16";
    }

    @PostMapping("/mbti16")
    public String mbti16Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti17";
    }

    @GetMapping("/mbti17")
    public String mbti17(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "더 동의하는 쪽을 선택하세요. <br>"
                + "1. 살인마 뿐만 아니라 귀신도 나에게 해를 입힐 수 있다. <br>"
                + "2. 귀신은 나에게 해를 입힐 수 없다. 사람만 조심하면 된다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti17";
    }

    @PostMapping("/mbti17")
    public String mbti17Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti18";
    }

    @GetMapping("/mbti18")
    public String mbti18(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "눈을 감고 머리를 감는 상황이다. <br>"
                + "눈을 뜰 수 없는 상황에서 앞에서 무언가 느껴진다. <br>"
                + "무엇이 상상되는가? <br>"
                + "1. 귀신 <br>"
                + "2. 괴물 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti18";
    }

    @PostMapping("/mbti18")
    public String mbti18Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setHG(mbtiTester.getHG()+1);
        } else if (answer == 2) {
            mbtiTester.setHG(mbtiTester.getHG()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti19";
    }

    @GetMapping("/mbti19")
    public String mbti19(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "빙의는 허무맹랑한 지어낸 헛소문일 뿐이다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti19";
    }

    @PostMapping("/mbti19")
    public String mbti19Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti20";
    }

    @GetMapping("/mbti20")
    public String mbti20(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "공포는 그저 도파민을 위한 수단일 뿐 그 이상도 이하도 아니다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti20";
    }

    @PostMapping("/mbti20")
    public String mbti20Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti21";
    }

    @GetMapping("/mbti21")
    public String mbti21(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "귀신을 봤다는 것은 그저 피곤하거나 헛것을 본 것일 뿐 의미부여 할 필요 없다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti21";
    }

    @PostMapping("/mbti21")
    public String mbti21Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti22";
    }

    @GetMapping("/mbti22")
    public String mbti22(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "사람에게는 기라는 것이 존재한다고 생각한다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti22";
    }

    @PostMapping("/mbti22")
    public String mbti22Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti23";
    }

    @GetMapping("/mbti23")
    public String mbti23(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "정말 오래된 건물이 있다 <br>"
                + "해당 건물은 저주받았다는 소문에 아무도 가지 않는다. <br>"
                + "심지어 폐가가 된 지 엄청나게 오래됐다. <br>"
                + "그 건물에 들어가면 저주받는다는 소문이 있는데 <br>"
                + "가서 하룻밤만 자고오면 갔다오고 한달 뒤에 1억을 준다고한다. <br>"
                + "갔다와서 한달 뒤에 준다는 말이 찝찝하긴 하지만 1억이 수중에 들어온다고 생각하니 고민된다. <br>"
                + "어떻게 할까? <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti23";
    }

    @PostMapping("/mbti23")
    public String mbti23Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti24";
    }

    @GetMapping("/mbti24")
    public String mbti24(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "나는 미신을 믿는다. (미신이 내 행동에 영향을 미친다.) <br>"
                + "예시) 의자를 빼놓고 자면 의자에 귀신이 앉아서 지켜본다. <br>"
                + "예시2) 문을 닫거나 제대로 열지 않고 틈을 만들어 놓으면 틈새로 무언가 지켜본다. <br>"
                + "예시3) 베개를 세워놓으면 부모님이 일찍 돌아가신다. <br>"
                + "예시4) 다리를 떨면 복이 떨어진다. -> 실제로는 좋은 효과밖에 없음 <br>"
                + "이에 동의하는가? <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester); // 얘만 있으면 안됨 -> session에서 꺼내와야 하므로 윗줄에 session에서 꺼내오는 과정이 분명히 필요함 -> dto 자체를 가져왔다기보다
        //           dto가 basepackages에 의해 bean으로 담겨있어 바로 불러올 수 있기 때문에?? 불러와서 그 객체는 계승한다고 했기때문에 사용가능?

        return "mbti24";
    }

    @PostMapping("/mbti24")
    public String mbti24Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester"); // 처음에는 session.getAttribute만 했는데 쌤한테 배운것과 여러가지로 이렇게 형변환과 함께 사용하는게 맞았다.
        // 이제 post쪽에서 값을 계속 불러오고 저장하기 위해서 get과 if로 처리후에 set을 반복할 것이다.
        if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        // 여기에서 만든 mbtiTester 객체를 저장하기 위해 세션을 이용해따 (이것도 마찬가지로 매개변수에 넣어야 사용할 수 있다.)
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti25";
    }

    @GetMapping("/mbti25")
    public String mbti25(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "우리나라에서 진행하는 굿과 외국에서 하는 퇴마의식 등을 보았을 때 <br>"
                + "분명 무언가 존재하는 것이기 때문에 나는 이러한 의식을 믿는다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti25";
    }

    @PostMapping("/mbti25")
    public String mbti25Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti26";
    }

    @GetMapping("/mbti26")
    public String mbti26(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "집을 구매하려고 하는데 두개의 매물이 있다. <br>"
                + "무엇을 선택할까? <br>"
                + "1. 소문이 안좋고 저주받았다는 소문의 아무도 살지 않아온 100평의 집 (이전 주인이 실종됐고 새로 들어온 주인도 실종됐다는 소문) <br>"
                + "2. 아무 문제없지만 비싼 25평의 집 (아무 소문도 없지만 오래된 집) <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti26";
    }

    @PostMapping("/mbti26")
    public String mbti26Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setSE(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setSE(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti27";
    }

    @GetMapping("/mbti27")
    public String mbti27(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "공포는 좋아하지만 점프스퀘어(갑툭튀)는 싫다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti27";
    }

    @PostMapping("/mbti27")
    public String mbti27Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti28";
    }

    @GetMapping("/mbti28")
    public String mbti28(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "공포의 진정한 묘미는 시각적인 것 보다 청각적으로 더 많이 작용한다고 생각한다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti28";
    }

    @PostMapping("/mbti28")
    public String mbti28Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti29";
    }

    @GetMapping("/mbti29")
    public String mbti29(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "잔잔한 공포보다는 극단적인 공포가 좋다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti29";
    }

    @PostMapping("/mbti29")
    public String mbti29Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti30";
    }

    @GetMapping("/mbti30")
    public String mbti30(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "두가지의 공포 영화 중 하나를 꼭 골라봐야 한다. <br>"
                + "무엇을 볼까? <br>"
                + "1. 귀신이야기 <br>"
                + "2. 살인마와의 추격전 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti30";
    }

    @PostMapping("/mbti30")
    public String mbti30Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti31";
    }

    @GetMapping("/mbti31")
    public String mbti31(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "영화를 볼 때 <br>"
                + "귀신이 나온 직후보다 나오기 전을 더 즐긴다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti31";
    }

    @PostMapping("/mbti31")
    public String mbti31Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti32";
    }

    @GetMapping("/mbti32")
    public String mbti32(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "공포감을 주는데 있어서 BGM이 없이 공포감을 주는 것은 불가능에 가깝다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti32";
    }

    @PostMapping("/mbti32")
    public String mbti32Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti33";
    }

    @GetMapping("/mbti33")
    public String mbti33(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "점프스퀘어(갑툭튀)에서 사람을 놀라게 하는 요소는 <br>"
                + "청각적인 요소보다 시각적인 요소이다. <br>"
                + "1. 예 <br>"
                + "2. 아니오 <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti33";
    }

    @PostMapping("/mbti33")
    public String mbti33Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti34";
    }

    @GetMapping("/mbti34")
    public String mbti34(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "공포게임 중 진정한 명작이라함은 <br>"
                + "1. 분위기의 연출이 뛰어난 게임이다. <br>"
                + "2. 분위기 뿐만 아니라 시각적인 뛰어남이 필요하다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti34";
    }

    @PostMapping("/mbti34")
    public String mbti34Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbti35";
    }

    @GetMapping("/mbti35")
    public String mbti35(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "공포 장르에서 내가 중요하다고 생각하는 것은 <br>"
                + "1. 진정한 공포게임의 명작이라함은 분위기의 연출이다. <br>"
                + "2. 진정한 공포게임은 분위기 뿐만 아니라 시각적인 뛰어남이 필요하다. <br>"
                + "당신의 선택은 ? ");
        model.addAttribute("mbtiTester", mbtiTester);

        return "mbti35";
    }

    @PostMapping("/mbti35")
    public String mbti35Answer(@RequestParam int answer, HttpSession session) {
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        if (answer == 2) {
            mbtiTester.setTM(mbtiTester.getSE()+1);
        } else if (answer == 1) {
            mbtiTester.setTM(mbtiTester.getSE()-1);
        }
        session.setAttribute("mbtiTester", mbtiTester); // 여기다가 저장을 하고 ?
        return "redirect:/mbtiResult";
    }

    @GetMapping("/mbtiResult")
    public void mbtiReult(Model model, HttpSession session) {
        model.addAttribute("mbtiMessage", "당신의 공포 MBTI<br>");
        MbtiTester mbtiTester = (MbtiTester) session.getAttribute("mbtiTester");
        String mbti = "";
        if (mbtiTester.getCB() >= 1) {
            mbti += "C";
        } else if (mbtiTester.getCB() <= -1) {
            mbti += "B";
        }
        if (mbtiTester.getHG() >= 1) {
            mbti += "H";
        } else if (mbtiTester.getHG() <= -1) {
            mbti += "G";
        }
        if (mbtiTester.getSE() >= 1) {
            mbti += "S";
        } else if (mbtiTester.getSE() <= -1) {
            mbti += "E";
        }
        if (mbtiTester.getTM() >= 1) {
            mbti += "T";
        } else if (mbtiTester.getTM() <= -1) {
            mbti += "M";
        }
        model.addAttribute("mbtiTester", "당신의 mbti는" + mbti);
        if(mbti.equals("CHET")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 사람 | 공포 : 즐김 | 선호 : 공포스릴 => 신중한 ");
        } else if (mbti.equals("CHEM")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 사람 | 공포 : 즐김 | 선호 : 공포분위기");
        } else if (mbti.equals("CHST")) {
            model.addAttribute("information", "성향 : 신중 | 두려움 : 사람 | 공포 : 진지 | 선호 : 공포스릴");
        } else if (mbti.equals("CHSM")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 사람 | 공포 : 진지 | 선호 : 공포분위기 => 프로파일러형");
        } else if (mbti.equals("CGST")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 귀신 | 공포 : 진지 | 선호 : 공포스릴 => 퇴마형");
        } else if (mbti.equals("CGSM")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 귀신 | 공포 : 진지 | 선호 : 공포분위기");
        } else if (mbti.equals("CGET")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 귀신 | 공포 : 즐김 | 선호 : 공포스릴");
        } else if (mbti.equals("CGEM")) {
            model.addAttribute("information","성향 : 신중 | 두려움 : 귀신 | 공포 : 즐김 | 선호 : 공포분위기");
        } else if (mbti.equals("BHST")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 사람 | 공포 : 진지 | 선호 : 공포스릴");
        } else if (mbti.equals("BHSM")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 사람 | 공포 : 진지 | 선호 : 공포분위기");
        } else if (mbti.equals("BHET")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 사람 | 공포 : 즐김 | 선호 : 공포스릴");
        } else if (mbti.equals("BHEM")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 사람 | 공포 : 즐김 | 선호 : 공포분위기");
        } else if (mbti.equals("BGST")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 귀신 | 공포 : 진지 | 선호 : 공포스릴 => 퇴마사형");
        } else if (mbti.equals("BGSM")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 귀신 | 공포 : 진지 | 선호 : 공포분위기");
        } else if (mbti.equals("BGET")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 귀신 | 공포 : 즐김 | 선호 : 공포스릴");
        } else if (mbti.equals("BGEM")) {
            model.addAttribute("information","성향 : 용감 | 두려움 : 귀신 | 공포 : 즐김 | 선호 : 공포분위기");
        }
    }

}
