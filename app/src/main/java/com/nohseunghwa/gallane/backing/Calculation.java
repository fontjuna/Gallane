package com.nohseunghwa.gallane.backing;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.nohseunghwa.gallane.backing.Constants.BRACKET_END;
import static com.nohseunghwa.gallane.backing.Constants.BRACKET_LEFT;
import static com.nohseunghwa.gallane.backing.Constants.BRACKET_RIGHT;
import static com.nohseunghwa.gallane.backing.Constants.BRACKET_START;
import static com.nohseunghwa.gallane.backing.Constants.CONVERT_FROM;
import static com.nohseunghwa.gallane.backing.Constants.CONVERT_TO;
import static com.nohseunghwa.gallane.backing.Constants.MINUS;
import static com.nohseunghwa.gallane.backing.Constants.PLUS;


/**
 * Created by fontjuna on 2017-08-27.
 */

public class Calculation {
    private static Calculation instance = null;

    private Calculation() {
    }

    // 싱글톤
    private static Calculation getInstance() {
        if (instance == null) {
            instance = new Calculation();
        }
        return instance;
    }

    // 외부에서 요청 될 계산 함수
    public static String Calculate(String data) {
        return Calculation.getInstance().excute(data);
    }

    // 내부에서 불리는 계산 함수
    private String excute(String data) {
        //공백제거 필요한 치환
        data = prepareData(data);
        //괄호 쌍과 데이타 끝을 검사 (데이타 끝은 숫자이거나 오른쪽 괄호이어야 함)
        if (!checkBracketPair(data)) {
            throw new RuntimeException("Calculation Error");
        }
        ArrayList<String> tokenList = new ArrayList<>();
        tokenList = splitByOperator(data);
        //계산호출
        try {
            tokenList = stackCalc(tokenList);
            if (tokenList.size() < 1) {
                throw new RuntimeException("Calulation Error");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Calulation Error");
        }
        return tokenList.get(0);
    }

    //계산 시작
    private ArrayList<String> stackCalc(ArrayList<String> tokenList) {
        // 첫문자가 "0-9,(,√,+,-"이외는 에러
        if (!Pattern.matches("^[0-9(√+-]*$", tokenList.get(0).substring(0, 1))) {
            throw new RuntimeException("Calculate Error");
        }
        ArrayList<String> resultList = new ArrayList<>();

//        // "√, !"의 싱글 오퍼레이터 검사 및 계산
//        tokenList = checkAncCalcSingleOperator(tokenList);

        if (tokenList.contains("(")) {
            int[] bracketPosition = findBracketPosition(tokenList, 0);
            // 괄호안에 데이타가 있다면
            if (bracketPosition[BRACKET_END] < bracketPosition[BRACKET_START] - 2) {
                //없으면
                throw new RuntimeException("Calculation Error");
            } else {
                //있으면
                ArrayList<String> centerList = new ArrayList<>();
                // 괄호안의 token만 담는다
                for (int i = bracketPosition[BRACKET_START] + 1; i < bracketPosition[BRACKET_END]; i++) {
                    centerList.add(tokenList.get(i));
                }
                // 괄호 안의 token 만 계산시킴
                centerList = stackCalc(centerList);
                //괄호가 다 없어지면 괄호 이전 token 과 괄호안의 계산 결과와 괄호 이후 token을 합침
                resultList = replaceCenterList(tokenList, bracketPosition[BRACKET_START], bracketPosition[BRACKET_END], centerList);
            }
        } else {
            // "√, !"의 싱글 오퍼레이터 검사 및 계산
            tokenList = checkAncCalcSingleOperator(tokenList);
            // 연산자 우선 순위 위치 파악 및 계산
            int calcPosition = findCalcPosition(tokenList);
            if (calcPosition > 0) {
                double value = calculateByOpCode(
                        Double.parseDouble(tokenList.get(calcPosition - 1)),
                        Double.parseDouble(tokenList.get(calcPosition + 1)),
                        tokenList.get(calcPosition));
                tokenList.set(calcPosition - 1, String.valueOf(value));
                tokenList.set(calcPosition, "");
                tokenList.set(calcPosition + 1, "");
            }
            // 데이타가 남은 token만 다시 리스트에 담는다.
            for (String s : tokenList) {
                if (!s.isEmpty()) {
                    resultList.add(s);
                }
            }
        }

        if (resultList.size() <= 1) {
            // 계산이 끝났다면 리스트의 갯수는 1
            return resultList;
        } else {
            // 아니면 다시 계산 시킴
            return stackCalc(resultList);
        }
    }

    private ArrayList<String> checkAncCalcSingleOperator(ArrayList<String> tokenList) {
        String[] singleOp = {"√", "!"};
        if (hasSingleOperator(tokenList)) {
            for (String op : singleOp) {
                tokenList = calcSingleOperator(tokenList, op);
            }
        }
        return tokenList;
    }

    private ArrayList<String> calcSingleOperator(ArrayList<String> tokenList, String op) {
        ArrayList<String> resultList = new ArrayList<>();
        int location = -1;
        int[] braket = {-1, -1};

        // 연산자 위치 찾아온다
        location = tokenList.indexOf(op);
        if (location > -1) {
            //괄호위치 찾아본다
            braket = findBracketPosition(tokenList, location);
            ArrayList<String> newTokenList = new ArrayList<>();
            if (braket[BRACKET_END] < 2) {
                braket[BRACKET_END] = location + 1;
                newTokenList.add(tokenList.get(location + 1));
            } else {
                for (int i = braket[BRACKET_START] + 1; i < braket[BRACKET_END]; i++) {
                    newTokenList.add(tokenList.get(i));
                }
                newTokenList = stackCalc(newTokenList);
            }
            double value = calculateByOpCode(Double.parseDouble(newTokenList.get(0)), 0.0, op);
            resultList.add(String.valueOf(value));
        }
        return replaceCenterList(tokenList, location, braket[BRACKET_END], resultList);
    }

    private boolean hasSingleOperator(ArrayList<String> tokenList) {
        return (tokenList.contains("√") || tokenList.contains("!"));
    }

    private boolean isSingleOperator(String operator) {
        return ("√".equals(operator) || "!".equals(operator));
    }

    private ArrayList<String> replaceCenterList(ArrayList<String> tokenList,
                                                int start,
                                                int end,
                                                ArrayList<String> centerList) {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < start; i++) {
            resultList.add(tokenList.get(i));
        }
        for (String s : centerList) {
            resultList.add(s);
        }
        for (int i = end + 1; i < tokenList.size(); i++) {
            resultList.add(tokenList.get(i));
        }
        return resultList;
    }

    private int findCalcPosition(ArrayList<String> tokenList) {
        int firstRun = -1;
        int level = -1;
        int num = 0;
        for (int i = num; i < tokenList.size(); i++) {
            if ("^".equals(tokenList.get(i))) {
                if (level < 2) {
                    level = 2;
                    firstRun = i;
                }
            } else if ("*".equals(tokenList.get(i))) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("/".equals(tokenList.get(i))) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("%".equals(tokenList.get(i))) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("+".equals(tokenList.get(i))) {
                if (level < 0) {
                    level = 0;
                    firstRun = i;
                }
            } else if ("-".equals(tokenList.get(i))) {
                if (level < 0) {
                    level = 0;
                    firstRun = i;
                }
            }
        }
        return firstRun;
    }

    // 처음 나오는 괄호의 시작 위치 끝위치 찾기
    private int[] findBracketPosition(ArrayList<String> tokenList, int start) {
        int[] bracketPosition = {-1, -1};
        int bracketCount = 0;
        for (int i = start; i < tokenList.size(); i++) {
            if (BRACKET_LEFT.equals(tokenList.get(i))) {
                bracketCount++;
                if (bracketPosition[BRACKET_START] < 0) {
                    bracketPosition[BRACKET_START] = i;
                }
            } else if (BRACKET_RIGHT.equals(tokenList.get(i))) {
                bracketCount--;
                if (bracketCount == 0) {
                    bracketPosition[BRACKET_END] = i;
                    break;
                }
            }
        }
        return bracketPosition;
    }

    // 입력된 문자열을 숫자 및 연산자 등으로 각각 분리해서 리스트로 만든다
    private ArrayList<String> splitByOperator(String data) {
        ArrayList tokenList = new ArrayList();
        String digits = "";
        for (int i = 0; i < data.length(); i++) {
            if (isDigitOrDot(data.charAt(i))) {
                // 수 라면
                digits += data.substring(i, i + 1);
            } else {
                // 수가 아니라면
                if (digits.isEmpty()) {
                    // 처음 이거나, 앞이 숫자가 아니었음(연산자)
                    if ((MINUS.equals(data.substring(i, i + 1)) || PLUS.equals(data.substring(i, i + 1))) &&
                            !tokenList.isEmpty() && isSingleOperator((String) tokenList.get(tokenList.size() - 1))) {
                        // 부호이거나 (이 경우 숫자에 포함)
                        digits += data.substring(i, i + 1);
                    } else {
                        tokenList.add(data.substring(i, i + 1));
                    }
                } else {
                    // 숫자가 있으므로 숫자 저장과 연산자 저장
                    tokenList.add(digits);
                    tokenList.add(data.substring(i, i + 1));
                    digits = "";
                }
            }
        }
        if (!digits.isEmpty()) {
            tokenList.add(digits);
        }
        return tokenList;
    }

    private boolean isSign(String operator, String preOperator) {
        boolean isSign = false;
        if ("+-".contains(operator)) {
            if ("√()*/+-".contains(preOperator)) {
                isSign = true;
            } else {
                throw new RuntimeException("Calculate Error");
            }
        } else if ("*/".contains(operator)) {
        }

        return isSign;
    }

    // 숫자 계산
    private double calculateByOpCode(double op1, double op2, String opcode) {
        if ("+".equals(opcode)) {
            //더하기
            return op1 + op2;
        } else if ("-".equals(opcode)) {
            //빼기;
            return op1 - op2;
        } else if ("*".equals(opcode)) {
            //곱하기
            return op1 * op2;
        } else if ("/".equals(opcode)) {
            //나누기, 반올림은 지정된 수
            return op1 / op2;
        } else if ("^".equals(opcode)) {
            //제곱
            return Math.pow(op1, op2);
        } else if ("%".equals(opcode)) {
            //나머지
            return op1 % op2;
        } else if ("√".equals(opcode)) {
            //루트
            return Math.sqrt(op1);
 /*       } else if ("!".equals(opcode)) {
            //팩토리얼
            return factorial(op1);
*/
        }
        throw new RuntimeException("Operation Error");
    }

/*
    private double factorial(double input) {
        if (input == 1.0) {
            return 1.0;
        }
        return factorial(input - 1) * input;
    }
*/

    /****************************************************/
    // 숫자 및 점 이외는 false
    private boolean isDigitOrDot(char token) {
        return (Character.isDigit(token) || token == 46);  // 46 = dot
    }

    // 받은 문자열에서 공백은 없애고, 필요한 변환 작업 실행
    private String prepareData(String data) {
        //계산 식 안의 빈칸을 없앤다.
        data = data.replace(" ", "");
        //괄호 앞 "*" 생략 처리, 단항 부호 "-","+" 처리(안됨)
        for (int i = 0; i < CONVERT_FROM.length; i++) {
            if (data.contains(CONVERT_FROM[i])) {
                data = data.replace(CONVERT_FROM[i], CONVERT_TO[i]);
            }
        }
        //앞에 부호있으면 변화
        if (data.charAt(0) == 43 || data.charAt(0) == 45) {
            data = "0" + data;
        }
        return data;
    }

    // 괄호 쌍이 맞는지 검사와 데이타 맨 오른쪽이 숫자이거나 괄호이어야 함
    private boolean checkBracketPair(String data) {
        char ch = data.charAt(data.length() - 1);   // 41="("
        return (isDigitOrDot(ch) || ch == 41)  // 데이타 끝이 숫자, 점, 오른쪽 괄호 이면서 쌍이 맞아야 됨
                && (data.replace("(", "").length() == data.replace(")", "").length());
    }

}
