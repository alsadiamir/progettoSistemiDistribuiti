setup-secrets:
	unzip -o secrets.zip -d .
	@cd Backend && chmod +x ./setup-env.sh && ./setup-env.sh
	@echo Done!

zip-secrets:
	@cd secrets && zip -r secrets.zip ./*
	@mv secrets/secrets.zip ./