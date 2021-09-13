package ru.otus.homework.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.otus.homework.props.CarLoanDictClientProperties;
import ru.otus.homework.dto.*;

import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CarLoanDictClient {

    private RestTemplate restTemplate;
    private CarLoanDictClientProperties props;

    private final String URL_PATH_REF_GROUP = "/reference-group/refgroup";
    private final String URL_PATH_REFERENCE = "/reference/reference";
    private final String URL_PATH_REF_ITEM = "/reference-item/refitem";
    private final String URL_PATH_BRAND = "/vehicle-brand/vehiclebrand";
    private final String URL_PATH_MODEL = "/vehicle-model/vehiclemodel";
    private final String URL_PATH_SET = "/vehicle-set/vehicleset";
    private final String URL_PATH_SET_YEAR = "/vehicle-set-year/vehiclesetyear";

    public CarLoanDictClient(RestTemplate restTemplate, CarLoanDictClientProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    public static File convert(MultipartFile file)
    {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convFile;
    }

    // Группы справочников

    public ResponseEntity<RefGroupPageResponseDTO> getReferenceGroupsByParams(
            Long parentId,
            String name,
            String sysname,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_GROUP + "s")
                        .queryParam("parentId", parentId)
                        .queryParam("name", name)
                        .queryParam("sysname", sysname)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RefGroupPageResponseDTO>() {}
        );

    }

    public ResponseEntity<RefGroupResDTO> getReferenceGroupById(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_GROUP + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RefGroupResDTO>() {});

    }

    public ResponseEntity<RefGroupResDTO> createReferenceGroup(RefGroupReqDTO refGroupReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_GROUP)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(refGroupReqDTO),
                new ParameterizedTypeReference<RefGroupResDTO>() {});

    }

    public ResponseEntity<RefGroupResDTO> updateReferenceGroup(Long id, RefGroupReqDTO refGroupReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_GROUP + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(refGroupReqDTO),
                new ParameterizedTypeReference<RefGroupResDTO>() {});

    }

    public ResponseEntity<Void> deleteReferenceGroup(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_GROUP + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>() {});

    }

    public ResponseEntity<List<RefGroupResDTO>> getAllReferenceGroups() {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_GROUP + "s/all")
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RefGroupResDTO>>() {}
        );

    }

    // Справочники

    public ResponseEntity<ReferencePageResponseDTO> getReferencesByParams(
            Long groupId,
            String name,
            String sysname,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REFERENCE + "s")
                        .queryParam("group.id", groupId)
                        .queryParam("name", name)
                        .queryParam("sysname", sysname)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ReferencePageResponseDTO>() {}
        );

    }

    public ResponseEntity<ReferenceResDTO> getReferenceById(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REFERENCE + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ReferenceResDTO>() {});

    }

    public ResponseEntity<ReferenceResDTO> createReference(ReferenceReqDTO referenceReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REFERENCE)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(referenceReqDTO),
                new ParameterizedTypeReference<ReferenceResDTO>() {});

    }

    public ResponseEntity<ReferenceResDTO> updateReference(Long id, ReferenceReqDTO referenceReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REFERENCE + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(referenceReqDTO),
                new ParameterizedTypeReference<ReferenceResDTO>() {});

    }

    public ResponseEntity<Void> deleteReference(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REFERENCE + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>() {});

    }

    public ResponseEntity<List<ReferenceResDTO>> getAllReferencesByGroupId(
            Long groupId
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REFERENCE + "s/all")
                        .queryParam("groupId", groupId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReferenceResDTO>>() {}
        );

    }

    // Элементы справочников

    public ResponseEntity<RefItemPageResponseDTO> getReferenceItemsByParams(
            Long referenceId,
            String code,
            String name,
            String brief,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_ITEM + "s")
                        .queryParam("reference.id", referenceId)
                        .queryParam("code", code)
                        .queryParam("name", name)
                        .queryParam("brief", brief)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RefItemPageResponseDTO>() {}
        );

    }

    public ResponseEntity<List<RefItemResDTO>> getAllReferenceItems(
            Long referenceId,
            String referenceSysName
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_ITEM + "s/all")
                        .queryParam("referenceId", referenceId)
                        .queryParam("referenceSysName", referenceSysName)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RefItemResDTO>>() {}
        );

    }

    public ResponseEntity<RefItemResDTO> getReferenceItemById(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_ITEM + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RefItemResDTO>() {});

    }

    public ResponseEntity<RefItemResDTO> createReferenceItem(RefItemReqDTO refItemReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_ITEM)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(refItemReqDTO),
                new ParameterizedTypeReference<RefItemResDTO>() {});

    }

    public ResponseEntity<RefItemResDTO> updateReferenceItem(Long id, RefItemReqDTO refItemReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_ITEM + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(refItemReqDTO),
                new ParameterizedTypeReference<RefItemResDTO>() {});

    }

    public ResponseEntity<Void> deleteReferenceItem(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_REF_ITEM + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>() {});

    }

    // Марки

    public ResponseEntity<VehicleBrandPageResponseDTO> browseVehicleBrandsByParams(
            String name,
            Long productionKindId,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_BRAND + "s")
                        .queryParam("name", name)
                        .queryParam("productionKind.id", productionKindId)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleBrandPageResponseDTO>() {}
        );

    }

    public ResponseEntity<List<VehicleBrandResDTO>> findVehicleBrandsByParams(
            MultiValueMap<String, String> headers,
            String name,
            Long productionKindId
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_BRAND + "s/all")
                        .queryParam("name", name)
                        .queryParam("productionKindId", productionKindId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<VehicleBrandResDTO>>() {}
        );

    }

    public ResponseEntity<VehicleBrandResDTO> getVehicleBrand(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_BRAND + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleBrandResDTO>() {});

    }

    public ResponseEntity<VehicleBrandResDTO> createVehicleBrand(VehicleBrandReqDTO vehicleBrandReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_BRAND)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(vehicleBrandReqDTO),
                new ParameterizedTypeReference<VehicleBrandResDTO>() {});

    }

    public ResponseEntity<VehicleBrandResDTO> modifyVehicleBrand(Long id, VehicleBrandReqDTO vehicleBrandReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_BRAND + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(vehicleBrandReqDTO),
                new ParameterizedTypeReference<VehicleBrandResDTO>() {});

    }

    public ResponseEntity<VehicleBrandReqDTO> deleteVehicleBrand(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_BRAND + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<VehicleBrandReqDTO>() {});

    }

    // Модели

    public ResponseEntity<VehicleModelPageResponseDTO> browseVehicleModelsByParams(
            Long brandId,
            String name,
            Long kindId,
            Long propertyFormId,
            Long productionKindId,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_MODEL + "s")
                        .queryParam("brand.id", brandId)
                        .queryParam("name", name)
                        .queryParam("kind.id", kindId)
                        .queryParam("ownForms.id", propertyFormId)
                        .queryParam("brand.productionKind.id", productionKindId)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleModelPageResponseDTO>() {}
        );

    }

    public ResponseEntity<List<VehicleModelResDTO>> findVehicleModelsByParams(
            Long brandId,
            String name,
            Long kindId,
            Long propertyFormId,
            Long productionKindId
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_MODEL + "s/all")
                        .queryParam("brandId", brandId)
                        .queryParam("name", name)
                        .queryParam("kindId", kindId)
                        .queryParam("propertyFormId", propertyFormId)
                        .queryParam("productionKindId", productionKindId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleModelResDTO>>() {}
        );

    }

    public ResponseEntity<VehicleModelResDTO> getVehicleModel(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_MODEL + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleModelResDTO>() {});

    }

    public ResponseEntity<VehicleModelResDTO> createVehicleModel(VehicleModelReqDTO vehicleModelReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_MODEL)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(vehicleModelReqDTO),
                new ParameterizedTypeReference<VehicleModelResDTO>() {});

    }

    public ResponseEntity<VehicleModelResDTO> modifyVehicleModel(Long id, VehicleModelReqDTO vehicleModelReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_MODEL + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(vehicleModelReqDTO),
                new ParameterizedTypeReference<VehicleModelResDTO>() {});

    }

    public ResponseEntity<VehicleModelReqDTO> deleteVehicleModel(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_MODEL + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<VehicleModelReqDTO>() {});

    }

    // Комплектации

    public ResponseEntity<VehicleSetPageResponseDTO> browseVehicleSetsByParams(
            Long modelId,
            Long bodyId,
            Long engineSizeId,
            Long engineTypeId,
            Long powerId,
            Long transmissionId,
            Long yearId,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET + "s")
                        .queryParam("model.id", modelId)
                        .queryParam("body.id", bodyId)
                        .queryParam("engineSize.id", engineSizeId)
                        .queryParam("engineType.id", engineTypeId)
                        .queryParam("power.id", powerId)
                        .queryParam("transmission.id", transmissionId)
                        .queryParam("years.year.id", yearId)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleSetPageResponseDTO>() {}
        );

    }

    public ResponseEntity<List<VehicleSetResDTO>> findVehicleSetsByParams(
            Long modelId,
            Long bodyId,
            Long engineSizeId,
            Long engineTypeId,
            Long powerId,
            Long transmissionId,
            Long yearId
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET + "s/all")
                        .queryParam("modelId", modelId)
                        .queryParam("bodyId", bodyId)
                        .queryParam("engineSizeId", engineSizeId)
                        .queryParam("engineTypeId", engineTypeId)
                        .queryParam("powerId", powerId)
                        .queryParam("transmissionId", transmissionId)
                        .queryParam("yearId", yearId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleSetResDTO>>() {}
        );

    }

    public ResponseEntity<CheckVehicleSetPriceResDTO> checkVehicleSetPrice(
            Long modelId,
            String characteristicSysName,
            Long bodyId,
            Long engineSizeId,
            Long engineTypeId,
            Long powerId,
            Long transmissionId,
            Long yearId
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET + "s/checkprice")
                        .queryParam("modelId", modelId)
                        .queryParam("characteristicSysName", characteristicSysName)
                        .queryParam("bodyId", bodyId)
                        .queryParam("engineSizeId", engineSizeId)
                        .queryParam("engineTypeId", engineTypeId)
                        .queryParam("powerId", powerId)
                        .queryParam("transmissionId", transmissionId)
                        .queryParam("yearId", yearId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CheckVehicleSetPriceResDTO>() {}
        );

    }

    public ResponseEntity<VehicleSetResDTO> getVehicleSet(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleSetResDTO>() {});

    }

    public ResponseEntity<VehicleSetResDTO> createVehicleSet(VehicleSetReqDTO vehicleSetReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(vehicleSetReqDTO),
                new ParameterizedTypeReference<VehicleSetResDTO>() {});

    }

    public ResponseEntity<VehicleSetResDTO> modifyVehicleSet(Long id, VehicleSetReqDTO vehicleSetReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(vehicleSetReqDTO),
                new ParameterizedTypeReference<VehicleSetResDTO>() {});

    }

    public ResponseEntity<VehicleSetReqDTO> deleteVehicleSet(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<VehicleSetReqDTO>() {});

    }

    // Года выпуска и стоимость для комплектаций

    public ResponseEntity<VehicleSetYearPageResponseDTO> browseVehicleSetYearsByParams(
            Long setId,
            Long yearId,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET_YEAR + "s")
                        .queryParam("set.id", setId)
                        .queryParam("year.id", yearId)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleSetYearPageResponseDTO>() {}
        );

    }

    public ResponseEntity<List<VehicleSetYearResDTO>> findVehicleSetYearsByParams(
            Long setId,
            Long yearId
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET_YEAR + "s/all")
                        .queryParam("setId", setId)
                        .queryParam("yearId", yearId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleSetYearResDTO>>() {}
        );

    }

    public ResponseEntity<VehicleSetYearResDTO> getVehicleSetYear(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET_YEAR + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<VehicleSetYearResDTO>() {});

    }

    public ResponseEntity<VehicleSetYearResDTO> createVehicleSetYear(VehicleSetYearReqDTO vehicleSetYearReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET_YEAR)
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(vehicleSetYearReqDTO),
                new ParameterizedTypeReference<VehicleSetYearResDTO>() {});

    }

    public ResponseEntity<VehicleSetYearResDTO> modifyVehicleSetYear(Long id, VehicleSetYearReqDTO vehicleSetYearReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET_YEAR + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(vehicleSetYearReqDTO),
                new ParameterizedTypeReference<VehicleSetYearResDTO>() {});

    }

    public ResponseEntity<VehicleSetYearReqDTO> deleteVehicleSetYear(Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path(URL_PATH_SET_YEAR + "/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<VehicleSetYearReqDTO>() {});

    }

    // Загрузка файлов

    public ResponseEntity<FileUploadHistoryPageResponseDTO> browseFileUploadHistoriesByParams(
            Long fileTypeId,
            String sort,
            Long size,
            Long page
    ) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/file-upload/history")
                        .queryParam("fileType.id", fileTypeId)
                        .queryParam("sort", sort)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<FileUploadHistoryPageResponseDTO>() {}
        );

    }

    public ResponseEntity<Void> uploadPassengerUsedVehicleModels(MultipartFile file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(convert(file)));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/file-upload/vehiclemodels/passenger-used")
                        .toUriString(),
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Void>() {});

    }

}
