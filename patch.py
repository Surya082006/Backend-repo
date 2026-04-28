import re

with open('src/main/java/com/klu/service/AuthService.java', 'r', encoding='utf-8') as f:
    text = f.read()

replacement = """        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    public Map<String, Object> googleLogin(String credential, String clientId) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                User user = repo.findByEmail(email).orElse(null);

                if (user != null) {
                    if (!user.isApproved()) {
                        throw new RuntimeException("Your account is pending approval from the Super Admin.");
                    }
                    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "SUCCESS");
                    response.put("token", token);
                    return response;
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "USER_NOT_FOUND");
                    response.put("email", email);
                    response.put("name", name);
                    return response;
                }
            } else {
                throw new RuntimeException("Invalid Google ID token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google Authentication Failed: " + e.getMessage());
        }
    }
}
"""

text = re.sub(r'        return jwtUtil\.generateToken\(user\.getEmail\(\), user\.getRole\(\)\);\s*\}\s*\}', replacement, text)

with open('src/main/java/com/klu/service/AuthService.java', 'w', encoding='utf-8') as f:
    f.write(text)
