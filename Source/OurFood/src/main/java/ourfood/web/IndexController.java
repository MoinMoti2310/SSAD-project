package ourfood.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ourfood.domain.Organization;
import ourfood.domain.Permissions;
import ourfood.domain.User;
import ourfood.service.UserService;
import ourfood.utils.ProjectUtil;
import ourfood.utils.QRCode;
import ourfood.utils.ValueTextOption;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * End point for frontoffice pages
 * 
 * \* @author raghu.mulukoju
 */
@RequestMapping(value = "")
@Controller
public class IndexController {

    @Autowired
    UserService userService;

    private static final Logger log = LogManager.getLogger(IndexController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home(Authentication authentication, HttpServletRequest request) {

        try {
            // Display scanon.in if root domain is accessed
            if (ProjectUtil.isScanonRootDomain(request.getRequestURL().toString())) {
                return "index";
            } else if (ProjectUtil.is5ndInfoDomain(request.getRequestURL().toString())) {
                // FIXME: Add a filter to all only root/cert path for 5nd.info domain
                return "error";
            }
        } catch (Exception ex) {
            log.warn("Unable to parse Request URL" + request.getRequestURI(), ex);
        }

        if (authentication != null && authentication.isAuthenticated()) {

            User authUser = (User) authentication.getPrincipal();

            if (authUser.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                return "dashboard";
            } else {

                String actionSetIds = authUser.getAuthorizedActionSetIds();
                String activeActionSetId = null;

                if (actionSetIds != null && !actionSetIds.isEmpty()) {
                    activeActionSetId = actionSetIds.split(",")[0];
                    return ("redirect:/as/details/" + activeActionSetId);
                } else {

                    // Get first ActionSet associated with user
                    User user = userService.getUser(authUser.getId());
                    Organization org = user.getOrganization();
                }
            }

            return "redirect: /un-authorized";
        }

        return "/user/login";
    }

    @RequestMapping(value = "/un-authorized", method = RequestMethod.GET)
    public String unAuthorized() {
        return "/un-authorized";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Authentication authentication, HttpServletRequest request) {

        try {
            // Display scanon.in if root domain is accessed
            if (ProjectUtil.isLocalhost(request.getRequestURL().toString())) {
                return "index";
            }
        } catch (Exception ex) {
            log.warn("Unable to parse Request URL" + request.getRequestURI(), ex);
        }

        return "/";
    }

    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public String terms(HttpServletRequest request) {
        return "terms";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact() {
        return "contact";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }

    @RequestMapping(value = "/intro", method = RequestMethod.GET)
    public String intro() {
        return "intro";
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.GET)
    public String feedback() {
        return "feedback";
    }

    @RequestMapping(value = "/blank", method = RequestMethod.GET)
    public String blank() {
        return "blank";
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public String feedback(@RequestParam String title, @RequestParam String message, @RequestParam String email,
            RedirectAttributes redirect) {
        userService.sendFeedback(title, message, email);
        redirect.addFlashAttribute("message", "Your feedback has been submitted");
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return "dashboard/dashboard";
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @RequestMapping(value = "/qr-code", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] qrCode(@RequestParam String code) {
        return QRCode.encode(code);
    }

    /**
     * Download QR Codes. NOTE: Authentication is removed.
     * 
     * @param code
     * @param size
     * @param filename
     * @param format
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/download-qr-code", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] downloadQrCode(@RequestParam String code, @RequestParam int size, @RequestParam String filename,
            @RequestParam String format, HttpServletResponse response) {

        // Set default file name
        if (filename == null || filename.isEmpty()) {
            filename = "Applite-QR-Code";
        }

        // Set default format
        if (format == null || format.isEmpty()) {
            format = "png";
        } else {
            // Validate format
            String supportedFormats = ",png,jpg,jpeg,jpe,bmp,";
            if (!supportedFormats.contains("," + format + ",")) {
                format = "png";
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + filename + "." + format);
        return QRCode.encode(code, size);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/construction", method = RequestMethod.GET)
    public String construction() {
        return "construction";
    }

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> healthCheck(HttpServletResponse response) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * For simple test executions
     * 
     * @param response
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/sandbox", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> sandbox(HttpServletResponse response) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/select2", method = RequestMethod.GET)
    @ResponseBody
    public Object select2(HttpServletResponse response, @RequestParam(required = false) String term) {

        Object obj = new Object();

        List<ValueTextOption> valueTextOptions = new ArrayList<ValueTextOption>();
        valueTextOptions.add(new ValueTextOption("1", "B.Tech"));
        valueTextOptions.add(new ValueTextOption("2", "B.Tech - ECE"));
        valueTextOptions.add(new ValueTextOption("3", "B.Tech - ECE - 1 Year"));

        return valueTextOptions;

        // HashMap<String, Object> ret = new HashMap<String, Object>(2);
        // ret.put("data", valueTextOptions);

        // return obj;
    }

    private void uploadFileToAws() {

        System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");

        // IAM (applite_s3_manager)
        // Access Key ID
        String accessKeyId = "AKIAJT2FA7TK5XXQCMTQ";
        // Secret Access Key
        String secretAccessKey = "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";

        // Bucket Name (applite-uploads)
        String bucketName = "scanonstatic";
        // String bucketName = "applite-uploads";
        // Bucket Key Name (file name)
        String keyName = "folder/aws-s3-upload.txt";
        // File
        File file = new File("C:/temp/aws-s3-upload.txt");

        try {

            BasicAWSCredentials cred = new BasicAWSCredentials(accessKeyId, secretAccessKey);
            AmazonS3 s3client = new AmazonS3Client(cred);
            s3client.putObject(new PutObjectRequest(bucketName, keyName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (Exception e) {
            System.out.println("Error in uploading file to AWS-S3:\n" + e.getStackTrace());
        }

        // Needs verification: IAM role may require AWSConnector permissions.
        // 3. Java System Properties: aws.accessKeyId and aws.secretKey. Use SystemPropertiesCredentialsProvider to
        // load the variables in your program
        // Examples:
        // https://javatutorial.net/java-s3-example
        // http://docs.aws.amazon.com/sdkfornet1/latest/apidocs/html/M_Amazon_S3_AmazonS3Client_PutObject.htm
    }

    private void saveMultipartFile(MultipartFile uploadfile) {

        try {

            MultipartFile uploadFile = (MultipartFile) uploadfile;

            // Get the filename and build the local file path (be sure that the
            // application have write permissions on such directory)
            String filename = uploadFile.getOriginalFilename();
            String directory = "C:/temp/uploaded_files";
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadFile.getBytes());
            stream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}