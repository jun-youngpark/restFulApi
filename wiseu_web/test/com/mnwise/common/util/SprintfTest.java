package com.mnwise.common.util;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Sprintf 테스트
 */
public class SprintfTest extends TestCase {
    @Test
    public void testSprintf() throws Exception {
        // eers_desc.append("148985").append('\t')
        // .append(Sprintf.format(" ",-30,' ')).append('\t')
        // .append(Sprintf.format(" ",-30,' ')).append('\t')
        // .append(rs.getString("USER_ID")).append('\t')
        // .append(Sprintf.format(" ",-10,' ')).append('\t')
        // .append(rs.getString("GEID")).append('\t')
        // .append(rs.getString("ROLE_NAME")).append('\t')
        // .append(rs.getString("ROLE_DESC")).append('\t')
        // .append(rs.getString("SOEID")).append('\t')
        // .append(Sprintf.format(" ",-255,' '));

        String userId = "";
        String geid = "";
        String roleName = "";
        String roleDesc = "";
        String soeid = "";

        StringBuffer sb = printEERS(userId, geid, roleName, roleDesc, soeid);

        System.out.println("[" + sb.toString() + "]");
    }

    /**
     * @param userId
     * @param geid
     * @param roleName
     * @param roleDesc
     * @param soeid
     * @return
     */
    private StringBuffer printEERS(String userId, String geid, String roleName, String roleDesc, String soeid) {
        StringBuffer sb = new StringBuffer();
        sb.append("148985").append('\t').append(Sprintf.format(" ", -30, ' ')).append('\t').append(Sprintf.format(" ", -30, ' ')).append('\t').append(userId).append('\t')
            .append(Sprintf.format(" ", -10, ' ')).append('\t').append(geid).append('\t').append(roleName).append('\t').append(roleDesc).append('\t').append(soeid).append('\t')
            .append(Sprintf.format(" ", -255, ' '));
        return sb;
    }

}
