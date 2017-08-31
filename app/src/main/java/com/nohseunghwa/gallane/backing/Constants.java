package com.nohseunghwa.gallane.backing;

/**
 * Created by fontjuna on 2017-08-20.
 */

public class Constants {

    /**
     * Created by fontjuna on 2017-08-15.
     */

    // 계산용
    public static final String MINUS = "-";
    public static final String PLUS = "+";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String CALC = PLUS + MINUS + DOT + COMMA;

    // 구분자 용
    public static final String ITEMnITEM = "&";
    public static final String TITLEnMONEY = "=";
    public static final String MEMBER2MEMBER = "~";
    public static final String LEFTnRIGHT = "@";
    public static final String MEMBERnMEMBER = ",";
    public static final String MEMBERnRATIO = "!";
    public static final String DELIMITER = ITEMnITEM + TITLEnMONEY + MEMBER2MEMBER + LEFTnRIGHT + MEMBERnMEMBER + MEMBERnRATIO;

    // 입력 텍스트
    public static final String DIGIT = "0-9";
    public static final String ENGLISH = "A-Za-z";
    public static final String KOREAN = "ㄱ-힣";
    public static final String TEXT = DIGIT + ENGLISH + KOREAN; // "0-9A-Za-zㄱ-힣"

    // 텍스트 조합
    public static final String CHARS = CALC + DELIMITER;
    public static final String TEXT_CHARS = TEXT + CHARS;       // "0-9A-Za-zㄱ-힣-+./:,!"

    // 입력 텍스트 검사
    public static final String VALID_CHARACTERS_ALL = String.format("^[%s]*$", TEXT_CHARS);
    public static final String VALID_CHARACTERS_TEXT = String.format("^[%s]*$", TEXT);
    public static final String VALID_CHARACTERS_RATIO = String.format("^[%s]*$", DIGIT + DOT);
    public static final String VALID_CHARACTERS_MEMBER = String.format("^[%s]*$", TEXT + MEMBERnMEMBER + MEMBERnRATIO + DOT + MEMBER2MEMBER);
    public static final String VALID_CHARACTERS_AMOUNT = String.format("^[%s]*$", DIGIT + DOT + COMMA + PLUS + MINUS); // MINUS가 젤 뒤로 와야 작동함

    // 메세지
    public static final String ERROR_WRONG_EXPRESSION = "수식에 불필요한 문자가 들어 있습니다.";
    public static final String ERROR_EMPTY_INPUT = "입력된 내용이 없습니다.";
    public static final String ERROR_INVALID = "입력된 내용이 규칙에 맞지 않습니다.";
    public static final String ERROR_DELIMITER = "구분자 사용이 잘 못 되었습니다.";
    public static final String ERROR_IN_MEMBER = "나눌 인원에 불필요한 문자가 있습니다.";
    public static final String ERROR_IN_RATIO = "나눌 배율에 불필요한 문자가 있습니다.";
    public static final String ERROR_IN_AMOUNT = "금액에 불필요한 문자가 있습니다.";
    public static final String ERROR_IN_DONT_DIVIDE = "금액과 나눌 사람들이 명확하지 않습니다.";

    public static final String HINT_INFORMATION
            = "구분자로   " + TITLEnMONEY +" " + LEFTnRIGHT + " "+MEMBER2MEMBER+"  " + MEMBERnMEMBER + "  " + MEMBERnRATIO + " " + ITEMnITEM + "  6개 문자를 사용합니다"
            + "\n타이틀 " + TITLEnMONEY +" 금액(또는 계산식) " + LEFTnRIGHT + "이름(또는 숫자 "+MEMBER2MEMBER+" 숫자) "
            + MEMBERnRATIO + " 배율 "  + MEMBERnMEMBER + " 이름 ... "+ ITEMnITEM + "기호 이후 패턴 반복"

            + "\n\n▣ 똑 같이 나눌 때( '" + LEFTnRIGHT + "' 로 금액과 사람 구분)"
            + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C (결과 A,B,C=4,000원)"

            + "\n\n▣ 똑 같이 나눌 때( '" + MEMBER2MEMBER + "' 로 사람들 지정)"
            + "\n  입력 12000" + LEFTnRIGHT + "1" + MEMBER2MEMBER +  "3 (결과 1,2,3=4,000원)"

            + "\n\n▣ 꼴찌한 횟수 만큼 낼때( '" + MEMBERnRATIO + "' 뒤에 횟수(배율))"
            + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnRATIO + "2" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "3 (" + MEMBERnRATIO + "1 은 없어도 같음)"
            + "\n   (결과 A=2,000/B=4,000/C=6,000원)"

            + "\n\n▣ 나눌 금액이 두가지 이상일 때( '" + ITEMnITEM + "' 로 구분)"
            + "\n  입력 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "3000" + LEFTnRIGHT + "C" + MEMBERnMEMBER + "D"
            + "\n    (결과 A,B=3,000/C=4,500/D=1,500원)"

            + "\n\n▣ C가 3,000원을 더내야 할 때"
            + "\n  입력 12000-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
            + "\n  또는 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
            + "\n    (결과 A,B=3,000/C=6,000원)"

            + "\n\n▣ C는 3,000원만 낼 때"
            + "\n  입력 12000-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
            + "\n  또는 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
            + "\n    (결과 A,B=4,500/C=3,000원)"

            + "\n\n▣ B의 몫을 C가 낼 때"
            + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnRATIO + "0" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "2"
            + "\n  또는 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "2 (B" + MEMBERnRATIO + "0 배율이 0은 생략)"
            + "\n    (결과 A=4,000/C=8,000원)"

            + "\n\n▣ B의 몫을 C,D가 낼 때"
            + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "1.5" + MEMBERnMEMBER + "D" + MEMBERnRATIO + "1.5"
            + "\n    (결과 A=3,000/B=0/C,D=4,500원)"

            + "\n\n▣ 찬조금액(=3,000원) 만큼 감해 줄 때"
            + "\n  입력 12000-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C"
            + "\n  또는 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C"
            + "\n  또는 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C"
            + "\n  (결과 각 3,000원)"

            + "\n\n▣ 더 복잡한 경우도 위의 예를 응용해 보세요"
            + "\n\n▣ 결과는 [가름전달]에서 메세지로 보낼수 있습니다."
            + "\n\n▣ 계산기를 사용하여 다른 계산도 할 수 있습니다."
            ;

    public static final String CALC_INPUT = "calc_input";
    public static final String CALC_PREVIUOS = "calc_previuos";
    public static final String CALC_RESULT = "calc_result";
    public static final String HINT_EXPRESSION = "타이틀" + TITLEnMONEY + "금액" + LEFTnRIGHT + "이름" + MEMBERnRATIO + "배율" + MEMBERnMEMBER + "...";
    public static final String INPUT_EXPRESSION = "input_expression";
    public static final String NO_BANKING = "\n\n(참고 :"
            + "\n예금주, 은행명, 계좌번호가 모두 있으면"
            + "\n계좌 정보가 덧붙여 보내 집니다.)"
            ;

    public static final String TAB_TITLE_0 = "가를것들";
    public static final String TAB_TITLE_1 = "쉬운가름";
    public static final String TAB_TITLE_2 = "가름전달";
    public static final String TAB_TITLE_3 = "계 산 기";

    //음수 연산 및 괄호앞 * 생략 처리
    public static final String[] CONVERT_FROM = {
            "--", "+-", "-+", "++",
            "0√", "1√", "2√", "3√", "4√", "5√", "6√", "7√", "8√", "9√", //"+√", "-√", //"√-", "√+" -> 이거 안됨 "√0+"가 되면 0+나 마찬가지
            "0(", "1(", "2(", "3(", "4(", "5(", "6(", "7(", "8(", "9(", "(-", "(+", //"+(", "-(",
//            "(-0", "(-1", "(-2", "(-3", "(-4", "(-5", "(-6", "(-7", "(-8", "(-9",
    };
    public static final String[] CONVERT_TO = {
            "+", "-", "-", "+",
            "0*√", "1*√", "2*√", "3*√", "4*√", "5*√", "6*√", "7*√", "8*√", "9*√", //"+1*√", "-1*√", //"√0-", "√0+"
            "0*(", "1*(", "2*(", "3*(", "4*(", "5*(", "6*(", "7*(", "8*(", "9*(", "(0-", "(0+",//"+1*(", "-1*(",
//            "(0-1*0", "(0-1*1", "(0-1*2", "(0-1*3", "(0-1*4", "(0-1*5", "(0-1*6", "(0-1*7", "(0-1*8", "(0-1*9",
    };
    //연산자가 아닌 기호
    public static final String[] BRACKET = {"(", ")", ","};
    //수 한 개가 필요한 연산기호(수는 왼쪽에 배치)
    public static final String[] SINGLE_OP = {"!"};
    //수 두 개가 필요한 연산기호(수는 양옆에 배치) - 왼쪽에서 오른쪽으로 계산한다.
    //예) 1 + 2 = 3, 6 / 3 = 2, 2 ^ 3 = 8..
    public static final String[] OPERATOR = {"+", "-", "*", "/", "^", "%"};
    //수가 필요없는 문자 연산기호
    public static final String[] WORD_CONSTANT = {"pi", "e"};
    //수 한 개가 필요한 문자 연산기호(괄호로 구분한다.)
    public static final String[] WORD_NEED_ONE = {"sin", "sinh", "asin", "cos", "cosh", "acos", "tan", "tanh", "atan",
            "sqrt", "exp", "abs", "log", "ceil", "floor", "round"};
    //수 두 개가 필요한 문자 연산기호(괄호, 콤마로 구분한다.)
    public static final String[] WORD_NEED_TWO = {"pow"};
    //나누기할 때 반올림 자릿수
    public static int HARF_ROUND_UP = 6;

    public static final int BRACKET_START = 0;
    public static final int BRACKET_END = 1;

    public static final String BRACKET_LEFT = "(";
    public static final String BRACKET_RIGHT = ")";
}
