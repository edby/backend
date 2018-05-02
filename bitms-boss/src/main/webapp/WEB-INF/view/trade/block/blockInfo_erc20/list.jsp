<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var blockInfoERC20DataGrid;
    $(function () {
        blockInfoERC20DataGrid = $('#blockInfoERC20DataGrid').datagrid({
            url: '${ctx}/block/blockinfoerc20/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            sortOrder: 'desc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
//                {
//                    width: '150',
//                    title: 'id',
//                    field: 'id'
//                },
                {
                    width: '70',
                    title: '区块高度',
                    field: 'height',
                    sortable:true
                },
                {
                    width: '80',
                    title: '交易扫描状态',
                    field: 'transScanStatus'
                },
                {
                    width: '439',
                    title: '区块哈希',
                    field: 'hash'
                },
                {
                    width: '439',
                    title: '父哈希',
                    field: 'parentHash'
                },

                {
                    width: '130',
                    title: '区块时间戳',
                    field: 'blockTimeStamp',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                },
                {
                    width: '500',
                    title: '备注',
                    field: 'remark'
                }

            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            }
        });

    });


    function searchBlockInfoERC20Fun() {
        try {
            blockInfoERC20DataGrid.datagrid('load',
                $.serializeObject($('#searchBlockInfoERC20Form')));
        } catch (err) {
        }
    }

    function cleanBlockInfoERC20Fun() {
        $('#searchBlockInfoERC20Form input').val('');
        blockInfoERC20DataGrid.datagrid('load', {});
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchBlockInfoERC20Form">
            <table>
                <tr>
                    <th>扫描状态</th>
                    <td>
                        <select id="transScanStatus" class="easyui-combobox" name="transScanStatus" style="width:100px;"
                                data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="true">true</option>
                            <option value="false">false</option>

                        </select>
                    </td>
                    <%--<th>开始时间:</th>--%>
                    <%--<td><input name="timeStart" class="easyui-datetimebox" placeholder="请输入开始时间"/></td>--%>
                    <%--<th>结束时间:</th>--%>
                    <%--<td><input name="timeEnd" class="easyui-datetimebox" placeholder="请输入结束时间"/></td>--%>
                    <%--<td>--%>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchBlockInfoERC20Fun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanBlockInfoERC20Fun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="blockInfoERC20DataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
