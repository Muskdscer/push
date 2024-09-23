package com.push.common.utils;

/**
 * Description:
 * Create DateTime: 2020/5/21 18:18
 *
 * 

 */
public class DeployUtils {

    private static final String pem = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEAzloRZS31uOlk0ynvx36F9rYmUK816IDcDSKXNC15PN+KqD3t\n" +
            "n3rATy7xyg/lIC8mYE4lycwPjAzj0wrouMmQ24oRIAMR+eR746WeVfz5Ufz9le8f\n" +
            "rUXNDMi4OgueGpyPmCnf3rZ4qSpq+n10d+yDYRkrBlSIRQ9F5qv41U2wtehglPhv\n" +
            "UdGNwsCTl7FdhK47GoQn7wVC8eFWJbQXUjWw/Y+Cyn2vJbeHrpXzAz+LGuSZ/D4N\n" +
            "z7JRT40NR7pMtd3zohAVo4jdFoLZb2IGM0fu5V+yduAZ27t/kS3CGYoUvsnv9Urt\n" +
            "nVrgxVkqUL8DW4+9xJMNr7ThnTCpOBae9PxhCwIDAQABAoIBAFKIK5TwQOtoTe83\n" +
            "QrPjouj+CW0T5tfLUjfeseMonOj/75FC05uJmQkSmZpbqSSqNwsltm9zsnQtY6Gx\n" +
            "J0thMxLbseW/1JzCCdsPDrkNqRpluXcSYPUIie6nCSgOJXo3TCdr+CI3kSL32Dm/\n" +
            "m6mraxCW5GJvxOD6LtcHyUpm/FqEkkW9+PBMYngIs0OFURn5Mw6mcVSn6ldApbTk\n" +
            "t6Lj3WR52opmBHvs67OIotjFi6J6zSlwgu6b4OjO8cICqpFKg3aDPzOPxPWy3Q36\n" +
            "Lh4V4TZhmvxyyUaORS9rn5dTSEWLpCVZDn4YU8FL9477Pjc1vWy7aAoFcjruMR1R\n" +
            "b4x9h6ECgYEA+VR3JXTXJBcqJKUl6ObQ+qAnAWgsBg7aAZSSxUHmrmEFFtEYMIhp\n" +
            "67pGxoaFuiqOIVfupQ8X62cHOJ97TDODelW5A29quCp/S/maHvtUO4td10guQQVy\n" +
            "TcvDbiG1Up7CbzrGNQIv/XHXEDSrckTrT7tF6/aVH427D13WC/8GaV0CgYEA099E\n" +
            "Zbv0VJwMJXZFF2l0RXDbLYdEIeZ58Nrw/ZtPg+q4I4ERR28nA/ucgFksAJlkV9zw\n" +
            "jZmqdX0rriD0xVMSqbro41X/guaL3cWPr6n8YU9ukE/OMTEAdLbB9n5EucHDgugY\n" +
            "yHmpDahwyZDRTIrnRAQi7s5cits5UlPNDBX8BYcCgYEAgAmfMe4BeXgn3S+t+x9l\n" +
            "G/9YaIGNcgL4cy1P/dps0wnhqijwaiPzeOls5RA55jNqT7cvRzYsC2gaLjq//47I\n" +
            "qu8QlrIFtNOmEWb5fYWFaRqVszNPBXb9jvPfxVOsIOSEZshg9uHLsTRfw+O4U3MP\n" +
            "AB/ktVsok06eTDLc88YgDcUCgYAicI9wrg24VylOSZxPgu/IfeDS1MMJc6pssVot\n" +
            "DzrsqZHHC6rgXrwL1LGK13PkoQqDkcF0cv8XXAJmqqq/DJLjDiPfGFKRV6JyqTOD\n" +
            "DXGd68iAIsjUjC+TyUVcKQhI3atHMy0cIVKOisvSp/ytoYKYNsfq45Z9Z8c4j7Cr\n" +
            "/0LZLwKBgQCVQ3bRyco/qSEJFykkStlkaNmhLF4m9EhPS0bGJKL3z2nAtxJ6yoP7\n" +
            "/so5CMrcrmhf+WYqzZq+odnLoQ/tkXahqcW45WagUGN0VeoZv9jJ2Tmykl1iT7If\n" +
            "FD3MvpdSn51KLZdxCuT3GEy8qK0hyrLkKP7GoaTnJlIGIKpEdf231A==\n" +
            "-----END RSA PRIVATE KEY-----";

    public static void main(String[] args) {
        updatePushOrderWeb();
        updatePushOrderBak();
    }

    private static void updatePushOrderWeb() {
        RemoteExecuteCommand rec = new RemoteExecuteCommand("47.114.190.255", "root", pem);
        String result = rec.execute("cd ~;sh updateweb-push-order.sh");
        System.out.println(result);
    }

    private static void updatePushOrderBak() {
        RemoteExecuteCommand rec = new RemoteExecuteCommand("47.114.190.255", "root", pem);
        String result = rec.execute("cd ~;sh bakautoupdate-push-order.sh");
        System.out.println(result);
    }
}
